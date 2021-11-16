package com.wyb.mq.rabbit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyb.mq.entity.OrderInfo;
import com.wyb.mq.service.IOrderInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(tags = "Rabbitmq分布式事务")
public class OrderController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @GetMapping("/saveOrder")
    public String saveOrder() throws JsonProcessingException {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(System.currentTimeMillis());
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setUserName("marcher");
        orderInfo.setMoney(10000);
        orderInfo.setProductNo(1);

        orderInfoService.saveOrderInfoWithMessage(orderInfo);
        return "ok";
    }
}
