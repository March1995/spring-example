package com.wyb.mq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.entity.OrderInfo;

/**
 * @vlog: 高于生活，源于生活
 * @desc: 类的描述:订单服务业务逻辑类
 * @author: smlz
 * @createDate: 2019/10/11 15:05
 * @version: 1.0
 */
public interface IOrderInfoService {

    /**
     * 方法实现说明:订单保存
     *
     * @param orderInfo:订单实体
     * @author:smlz
     * @return: int 插入的条数
     */
    void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent);

    void saveOrderInfoWithMessage(OrderInfo orderInfo) throws JsonProcessingException;
}
