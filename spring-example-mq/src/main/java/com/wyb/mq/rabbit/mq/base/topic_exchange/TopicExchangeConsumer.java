package com.wyb.mq.rabbit.mq.base.topic_exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicExchangeConsumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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

        //声明交换机
        String exchangeName = "tuling.topicexchange";
        String exchangeType = "topic";
        channel.exchangeDeclare(exchangeName,exchangeType,true,true,null);

        //声明队列
        String quequName = "tuling.topic.queue";
        channel.queueDeclare(quequName,true,false,false,null);

        //声明绑定关系
        String bingdingStr = "tuling.*";
        channel.queueBind(quequName,exchangeName,bingdingStr);

        //声明一个消费者
//        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //开始消费
        /**
         * 开始消费
         */
        channel.basicConsume(quequName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String reciverMessage = new String(body);
                System.out.println("消费消息:-----" + reciverMessage);
            }
        });
    }
}
