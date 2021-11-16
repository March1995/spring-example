package com.wyb.mq.mapper;


import com.wyb.mq.entity.OrderInfo;


public interface OrderInfoMapper {

    /**
     * 方法实现说明:订单保存
     *
     * @param orderInfo:订单实体
     */
    int saveOrderInfo(OrderInfo orderInfo);
}
