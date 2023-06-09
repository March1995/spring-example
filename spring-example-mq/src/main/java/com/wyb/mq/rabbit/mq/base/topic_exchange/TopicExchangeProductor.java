package com.wyb.mq.rabbit.mq.base.topic_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicExchangeProductor {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("111.231.85.51");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("test");
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("test");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        //创建channel
        Channel channel = connection.createChannel();

        //发送消息
        String exchangeName = "tuling.topicexchange";

        String routingKey1 = "tuling.key1";
        String routingKey2 = "tuling.key2";
        String routingKey3 = "tuling.key.key3";

        channel.basicPublish(exchangeName,routingKey1,null,"我是第一条消息".getBytes());
        channel.basicPublish(exchangeName,routingKey2,null,"我是第二条消息".getBytes());
        channel.basicPublish(exchangeName,routingKey3,null,"我是第三条消息".getBytes());
    }
}
