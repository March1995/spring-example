package com.wyb.mq.rabbit.mq.base;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Marcher丶
 * qos限流消费
 * <p>
 * rabbitmq 提供一个钟qos（服务质量保证）,也就是在关闭了消费端的自动ack的前提 下，我们可以设置阈值（出队）的消息数没有被确认（手动确认），那么就不会推送 消息过来.
 * 限流的级别(consumer级别或者是channel级别)
 * 实现的方式 void BasicQos(uint prefetchSize,ushort prefetchCount ,bool global)
 * uint prefetchSize ：指定的是设定消息的大小(rabbitmq还没有该功能，所以一般是填写0表示不限制)
 * ushort perfetchCount ：表示设置消息的阈值，每次过来几条消息(一般是填写1 一条 一条的处理消息)
 * bool global：表示是channel级别的还是 consumer的限制(channel的限制rabbitmq 还没有该功能)
 */
public class QosPushConsumer {

    private final static String QUEUE_NAME = "test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        while (true) {
            // 消费消息
            boolean autoAck = false;
            String consumerTag = "";
            // DefaultConsumer consumer = new DefaultConsumer(channel);
            // consumer.handleDelivery(QUEUE_NAME, autoAck, consumerTag, ;

            channel.basicConsume(QUEUE_NAME, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    System.err.println("-----------consume message----------");
                    System.err.println("consumerTag: " + consumerTag);
                    System.err.println("envelope: " + envelope);
                    System.err.println("properties: " + properties);
                    System.err.println("body: " + new String(body));

                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    // (process the message components here . .. )
                    channel.basicQos(0, 1, false);
                }
            });
        }
    }
}
