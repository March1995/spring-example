/*
 * @(#)Product    Created on 2020/7/21
 * Copyright (c) 2020 ZDSoft Networks, Inc. All rights reserved.
 * $$ Id$$
 */
package com.wyb.mq.rabbit.mq.base;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Marcher丶
 * @version $$ Revision: 1.0 $$, $$ Date: 2020/7/21 14:48 $$
 */
public class Producer {

    private final static String QUEUE_NAME = "test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 以前讲过说我们的消息会发送的exchange上，
         * 但是在这里我们没有指定交换机?那我们的消息发送到哪里了？？？？
         * The default exchange is implicitly bound to every queue, with a routing key equal to the queue name.
         * It is not possible to explicitly bind to, or unbind from the default exchange. It also cannot be deleted.
         * 说明:加入我们消息发送的时候没有指定具体的交换机的话，那么就会发送到rabbimtq指定默认的交换机上，
         * 那么该交换机就会去根据routing_key 查找对应的queueName 然后发送的该队列上.
         *
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 通过channel发送消息
        for (int i = 0; i < 5; i++) {
            String message = "Hello World!" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        channel.close();
        connection.close();
    }
}
