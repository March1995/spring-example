package com.wyb.mq.service.impl;

import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.entity.OrderInfo;
import com.wyb.mq.enumuration.MsgStatusEnum;
import com.wyb.mq.mapper.MsgContentMapper;
import com.wyb.mq.mapper.OrderInfoMapper;
import com.wyb.mq.rabbit.constants.RabbitConstants;
import com.wyb.mq.rabbit.mq.CustomCorrelationData;
import com.wyb.mq.rabbit.mq.sender.RabbitSender;
import com.wyb.mq.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Resource
    private RabbitSender rabbitSender;


    @Override
    @Transactional
    public void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent) {
        try {
            // 订单表
            orderInfoMapper.saveOrderInfo(orderInfo);
            log.info("下单成功,orderNo {}", orderInfo.getOrderNo());
            // 消息表
            msgContentMapper.saveMsgContent(messageContent);
            log.info("插入消息表成功,orderNo {}", orderInfo.getOrderNo());
//            System.out.println(1/0);
        } catch (Exception e) {
            throw new RuntimeException("操作数据库失败");
        }
    }

    @Override
    @Transactional() //开启事务
    public void saveOrderInfoWithMessage(OrderInfo orderInfo) {
        MessageContent message = buildMessageContent(orderInfo.getOrderNo(), orderInfo.getProductNo());
        // 本地事务
        saveOrderInfo(orderInfo, message);

        // 发送mq
        MsgTxtBo msg = new MsgTxtBo();
        msg.setMsgId(message.getMsgId());
        msg.setOrderNo(message.getOrderNo());
        msg.setProductNo(message.getProductNo());

        CustomCorrelationData data = new CustomCorrelationData();
        data.setMessage(msg);
        data.setExchange(message.getExchange());
        data.setRoutingKey(message.getRoutingKey());
        data.setMaxRetryCount(message.getMaxRetry());
        data.setId(message.getMsgId());
        rabbitSender.sendMessage(data);
    }

    private MessageContent buildMessageContent(long orderNo, Integer productNo) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgId(UUID.randomUUID().toString());
        messageContent.setOrderNo(orderNo);
        messageContent.setCreateTime(new Date());
        messageContent.setUpdateTime(new Date());
        messageContent.setMsgStatus(MsgStatusEnum.SENDING.getCode());
        messageContent.setExchange(RabbitConstants.MQ_EXCHANGE_ORDER_TO_PRODUCT);
        messageContent.setRoutingKey(RabbitConstants.MQ_ROUTING_KEY_ORDER_TO_PRODUCT);
        messageContent.setMaxRetry(RabbitConstants.MSG_RETRY_COUNT);
        messageContent.setProductNo(productNo);
        return messageContent;
    }
}
