package com.wyb.mq.rabbit.mq.base.fanout_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchangeProductor {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.159.8");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("tuling");
        connectionFactory.setUsername("smlz");
        connectionFactory.setPassword("smlz");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        //创建channel
        Channel channel = connection.createChannel();

        //定义交换机名称
        String exchangeName = "tuling.fanoutexchange";

        //定义routingKey
        String routingKey = "";

        //消息体内容
        String messageBody = "hello tuling ";
        channel.basicPublish(exchangeName,"123",null,"我是第一条消息".getBytes());
        channel.basicPublish(exchangeName,"456",null,"我是第二条消息".getBytes());
        channel.basicPublish(exchangeName,"789",null,"我是第三条消息".getBytes());
    }
}
