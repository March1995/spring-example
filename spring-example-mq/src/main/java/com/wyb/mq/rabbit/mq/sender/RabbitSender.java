package com.wyb.mq.rabbit.mq.sender;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.enumuration.MsgStatusEnum;
import com.wyb.mq.mapper.MsgContentMapper;
import com.wyb.mq.rabbit.mq.CustomCorrelationData;
import com.wyb.mq.rabbit.mq.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 消息发送
 *
 * @author Kunzite
 */
@Service
@Slf4j
public class RabbitSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, InitializingBean {

    public static final int MAX_RETRY_NUM = 3;

    /**
     * rabbit客户端
     */
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MsgContentMapper msgContentMapper;


    /**
     * 发送MQ消息 异步
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由名称
     * @param message      发送消息体
     */
    public void sendMessage(String exchangeName, String routingKey, Object message) {
        Assert.notNull(message, "message 消息体不能为NULL");
        Assert.notNull(exchangeName, "exchangeName 不能为NULL");
        Assert.notNull(routingKey, "routingKey 不能为NULL");

        // 获取CorrelationData对象
        CustomCorrelationData correlationData = this.customCorrelationData(message);
        correlationData.setExchange(exchangeName);
        correlationData.setRoutingKey(routingKey);
        correlationData.setMessage(message);
        log.info("发送MQ消息，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                correlationData.getId(), JSON.toJSONString(message), exchangeName, routingKey);
        // 发送消息
        this.convertAndSend(exchangeName, routingKey, message, correlationData);
    }

    public void sendMessage(CustomCorrelationData correlationData) {
        log.info("发送MQ消息，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                correlationData.getId(), JSON.toJSONString(correlationData.getMessage()), correlationData.getExchange(),
                correlationData.getRoutingKey());
        // 发送消息
        this.convertAndSend(correlationData.getExchange(), correlationData.getRoutingKey(), correlationData.getMessage(), correlationData);
    }


    /**
     * RPC方式，顺序消费 发送MQ消息
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由名称
     * @param message      发送消息体
     */
    public void sendAndReceiveMessage(String exchangeName, String routingKey, SendMessage message) {
        Assert.notNull(message, "message 消息体不能为NULL");
        Assert.notNull(exchangeName, "exchangeName 不能为NULL");
        Assert.notNull(routingKey, "routingKey 不能为NULL");
        // 获取CorrelationData对象
        CustomCorrelationData correlationData = this.customCorrelationData(message, message.getId());
        correlationData.setExchange(exchangeName);
        correlationData.setRoutingKey(routingKey);
        correlationData.setMessage(message);

        log.info("发送MQ消息，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                correlationData.getId(), JSON.toJSONString(message), exchangeName, routingKey);

        rabbitTemplate.convertSendAndReceive(exchangeName, routingKey, message);
    }


    /**
     * 用于实现消息发送到RabbitMQ交换器后接收ack回调。
     * 如果消息发送确认失败就进行重试。
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        CustomCorrelationData customCorrelationData = null;
        if (correlationData instanceof CustomCorrelationData) {
            customCorrelationData = (CustomCorrelationData) correlationData;

            if (customCorrelationData.getMdcContainer() != null) {
                // 日志链路跟踪
                MDC.setContextMap(customCorrelationData.getMdcContainer());
            }
        }


        // 消息回调确认失败处理
        if (!ack && customCorrelationData != null) {
            // 这里以做消息的重发等处理
            log.info("消息发送失败，消息ID:{}", correlationData.getId());

            //消息发送失败,就进行重试，重试过后还不能成功就记录到数据库
            if (customCorrelationData.getRetryCount() < customCorrelationData.getMaxRetryCount()) {
                log.info("MQ消息发送失败，消息重发，消息ID：{}，重发次数：{}，消息体:{}", customCorrelationData.getId(),
                        customCorrelationData.getRetryCount(), JSON.toJSONString(customCorrelationData.getMessage()));

                customCorrelationData.setRetryCount(customCorrelationData.getRetryCount() + 1);

                // 重发消息
                this.convertAndSend(customCorrelationData.getExchange(), customCorrelationData.getRoutingKey(),
                        customCorrelationData.getMessage(), customCorrelationData);
                // 更新消息重试次数
                msgContentMapper.updateMsgRetryCount(customCorrelationData.getId());
            } else {
                //消息重试发送失败,将消息放到数据库等待补发
                log.warn("MQ消息重发失败，消息入库，消息ID：{}，消息体:{}", correlationData.getId(),
                        JSON.toJSONString(customCorrelationData.getMessage()));

                // 保存消息到数据库
                updateMsgStatusWithNack(correlationData.getId(), cause);
            }
        } else {
            log.info("消息发送成功,消息ID:{}", correlationData.getId());
            log.info("mq发送成功,orderNo {}", ((MsgTxtBo) customCorrelationData.getMessage()).getOrderNo());
            updateMsgStatusWithAck(correlationData.getId());
        }
    }

    /**
     * 用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
     * 基本上来说线程不可能出现这种情况，除非手动将已经存在的队列删掉，否则在测试阶段肯定能测试出来。
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 反序列化消息
        Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
        if (msg instanceof SendMessage) {
            // 日志链路跟踪
            MDC.setContextMap(((com.wyb.mq.rabbit.mq.SendMessage) msg).getMdcContainer());
        }

        log.error("MQ消息发送失败，replyCode:{}, replyText:{}，exchange:{}，routingKey:{}，消息体:{}",
                replyCode, replyText, exchange, routingKey, JSON.toJSONString(message.getBody()));

        // 保存消息到数据库
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);
            log.info("无法路由消息内容:{},cause:{}", msgTxtBo, replyText);

            //构建消息对象
            MessageContent messageContent = new MessageContent();
            messageContent.setErrCause(replyText);
            messageContent.setUpdateTime(new Date());
            messageContent.setMsgStatus(MsgStatusEnum.SENDING_FAIL.getCode());
            messageContent.setMsgId(msgTxtBo.getMsgId());
            //更新消息表
            msgContentMapper.updateMsgStatus(messageContent);
        } catch (Exception e) {
            log.error("更新消息表异常:{}", e);
        }
    }

    /**
     * 消息相关数据（消息ID）
     *
     * @param message
     * @return
     */
    private CustomCorrelationData customCorrelationData(Object message) {
        return new CustomCorrelationData(UUID.randomUUID().toString(), message);
    }

    /**
     * 消息相关数据（消息ID）
     *
     * @param message
     * @return
     */
    private CustomCorrelationData customCorrelationData(Object message, String messageId) {
        // 消息ID默认使用UUID
        if (StringUtils.isEmpty(messageId)) {
            messageId = UUID.randomUUID().toString();
        }
        return new CustomCorrelationData(messageId, message);
    }

    /**
     * 发送消息
     *
     * @param exchange        交换机名称
     * @param routingKey      路由key
     * @param message         消息内容
     * @param correlationData 消息相关数据（消息ID）
     * @throws AmqpException
     */
    private void convertAndSend(String exchange, String routingKey, final Object message, CorrelationData correlationData) throws AmqpException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        } catch (Exception e) {
            log.error("MQ消息发送异常，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                    correlationData.getId(), JSON.toJSONString(message), exchange, routingKey, e);

            log.info("mq发送失败,orderNo {}", ((MsgTxtBo) message).getOrderNo());
            // 保存消息到数据库 发送失败
            updateMsgStatusWithNack(correlationData.getId(), e.getMessage());
        }
    }

    /**
     * 发送延时消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由key
     * @param message    消息内容
     */
    public void sendDelayMessage(String exchange, String routingKey, final Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay", 10000);//设置延迟时间
                return message;
            }
        });
    }

    @Override
    public void afterPropertiesSet() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    private void updateMsgStatusWithAck(String msgId) {
        MessageContent messageContent = builderUpdateContent(msgId);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING_SUCCESS.getCode());
        log.info("修改消息表状态为发送成功,msgId {}", msgId);
        msgContentMapper.updateMsgStatus(messageContent);
    }

    private void updateMsgStatusWithNack(String msgId, String cause) {
        MessageContent messageContent = builderUpdateContent(msgId);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING_FAIL.getCode());
        messageContent.setErrCause(cause);
        log.info("修改消息表状态为发送失败,msgId {}", msgId);
        msgContentMapper.updateMsgStatus(messageContent);
    }

    private MessageContent builderUpdateContent(String msgId) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgId(msgId);
        messageContent.setUpdateTime(new Date());
        return messageContent;
    }
}
