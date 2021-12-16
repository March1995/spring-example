package com.wyb.mq.rabbit.mq.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.wyb.cache.service.CacheService;
import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.entity.ProductInfo;
import com.wyb.mq.enumuration.MsgStatusEnum;
import com.wyb.mq.mapper.MsgContentMapper;
import com.wyb.mq.rabbit.constants.RabbitConstants;
import com.wyb.mq.rabbit.mq.exception.BizExp;
import com.wyb.mq.service.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Kunzite
 */
@Service
public class SendMessageListener extends AbstractConsumer {

    private final Logger logger = LoggerFactory.getLogger(SendMessageListener.class);

    public static final String LOCK_KEY = "MQ_CONSUMER:";

    @Resource
    private MsgContentMapper msgContentMapper;
    @Resource
    private IProductService productService;
    @Resource
    private CacheService redisService;

    @RabbitListener(queues = RabbitConstants.QUEUE_NAME_ORDER_TO_PRODUCT)
    public void process(MsgTxtBo msgTxtBo, Channel channel, Message message) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 加分布式锁控制重复消费
//        if (redisTemplate.opsForValue().setIfAbsent(LOCK_KEY + msgTxtBo.getMsgId(), msgTxtBo.getMsgId())) {
        if (redisService.tryLock(LOCK_KEY + msgTxtBo.getMsgId(), 0, 0)) {
            // onMessage(message);
            logger.info("[{}]处理优惠券队列消息队列接收数据，消息体：{}", RabbitConstants.QUEUE_NAME_ORDER_TO_PRODUCT, JSON.toJSONString(msgTxtBo));

            // 取库存
            ProductInfo productInfo = productService.getById(msgTxtBo.getProductNo());
            try {
                // 参数校验
                Assert.notNull(msgTxtBo, "sendMessage 消息体不能为NULL");

                if (null == productInfo) {
                    updateMsgConsumerSuccess(msgTxtBo.getMsgId());
                    channel.basicAck(deliveryTag, false);
                    logger.error("productNo {}不存在", msgTxtBo.getProductNo());
                    return;
                }
                if (productInfo.getProductNum() <=0) {
                    updateMsgConsumerSuccess(msgTxtBo.getMsgId());
                    channel.basicAck(deliveryTag, false);
                    logger.error("修改消息状态为消费成功，productNo {}库存为0", msgTxtBo.getProductNo());
                    return;
                }

                // 减库存
                productService.updateProductStore(msgTxtBo);

//                 System.out.println(1/0);
                // 确认消息已经消费成功 multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
                channel.basicAck(deliveryTag, false);
                logger.info("消费成功，orderNo {}， productNo {}", msgTxtBo.getOrderNo(), msgTxtBo.getProductNo());
            } catch (Exception e) {
                if (e instanceof BizExp) {
                    BizExp bizExp = (BizExp) e;
                    logger.info("数据业务异常:{},即将删除分布式锁", bizExp.getErrMsg());
                    //删除分布式锁
                    redisService.unlock(LOCK_KEY);
                }
                logger.error("MQ消息处理异常，消息体:{}", message.getMessageProperties().getCorrelationId(), JSON.toJSONString(msgTxtBo), e);

                //更新消息表状态
                updateMsgConsumerFail(msgTxtBo.getMsgId(), e.getMessage());

                // 确认消息已经消费消费失败，将消息发给下一个消费者
                // true 重新存入队列，发给下一个订阅者 false直接移除
                channel.basicReject(deliveryTag, false);
            }
        } else {
            logger.error("请勿重复消费,msgId:{}", msgTxtBo.getMsgId());
            channel.basicReject(deliveryTag, false);
        }

    }

    private void updateMsgConsumerSuccess(String msgId) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgId(msgId);
        messageContent.setUpdateTime(new Date());
        messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_SUCCESS.getCode());
        msgContentMapper.updateMsgStatus(messageContent);
        logger.info("修改消息表状态为消费成功,msgId {}", msgId);
    }

    private void updateMsgConsumerFail(String msgId,String errorMsg) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_FAIL.getCode());
        messageContent.setUpdateTime(new Date());
        messageContent.setErrCause(errorMsg);
        messageContent.setMsgId(msgId);
        msgContentMapper.updateMsgStatus(messageContent);
        logger.info("修改消息表状态为消费失败,msgId {}", msgId);
    }
}
