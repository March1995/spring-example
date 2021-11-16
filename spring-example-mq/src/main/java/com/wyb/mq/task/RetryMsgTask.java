package com.wyb.mq.task;

import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.enumuration.MsgStatusEnum;
import com.wyb.mq.mapper.MsgContentMapper;
import com.wyb.mq.rabbit.constants.RabbitConstants;
import com.wyb.mq.rabbit.mq.CustomCorrelationData;
import com.wyb.mq.rabbit.mq.sender.RabbitSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 循环处理放在定时任务里好 还是请求失败后就处理好 待商榷
 */
@Component
@Slf4j
public class RetryMsgTask {

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private MsgContentMapper msgContentMapper;

    /**
     * 延时5s启动
     * 周期10S一次
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void retrySend() {
        System.out.println("-----------------------------");
        //查询五分钟消息状态还没有完结的消息
        List<MessageContent> messageContentList = msgContentMapper.qryNeedRetryMsg(MsgStatusEnum.CONSUMER_SUCCESS.getCode(), RabbitConstants.TIME_DIFF);

        for (MessageContent messageContent : messageContentList) {

            if (messageContent.getMaxRetry() > messageContent.getCurrentRetry()) {
                MsgTxtBo msgTxtBo = new MsgTxtBo();
                msgTxtBo.setMsgId(messageContent.getMsgId());
                msgTxtBo.setProductNo(messageContent.getProductNo());
                msgTxtBo.setOrderNo(messageContent.getOrderNo());
                //更新消息重试次数
                msgContentMapper.updateMsgRetryCount(msgTxtBo.getMsgId());

                CustomCorrelationData data = new CustomCorrelationData();
                data.setMessage(msgTxtBo);
                data.setExchange(messageContent.getExchange());
                data.setRoutingKey(messageContent.getRoutingKey());
                data.setMaxRetryCount(messageContent.getMaxRetry());
                data.setId(messageContent.getMsgId());

                rabbitSender.sendMessage(data);
            } else {
                log.warn("消息:{}以及达到最大重试次数", messageContent);
            }

        }
    }
}
