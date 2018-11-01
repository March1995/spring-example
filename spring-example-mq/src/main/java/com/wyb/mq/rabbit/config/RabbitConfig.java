package com.wyb.mq.rabbit.config;

import com.wyb.mq.rabbit.constants.RabbitConstants;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列常量
 *
 * @author Kunzite
 */
@Configuration
@ConditionalOnBean({RabbitTemplate.class})
public class RabbitConfig {

    /**
     * 动态声明 queue，exchange，routing
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        // 生成服务端
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        //声明死信队列（Fanout类型的exchange）
        Queue deadQueue = new Queue(RabbitConstants.QUEUE_NAME_DEAD_QUEUE);
        // 死信队列交换机
        FanoutExchange deadExchange = new FanoutExchange(RabbitConstants.MQ_EXCHANGE_DEAD_QUEUE);
        rabbitAdmin.declareQueue(deadQueue);
        rabbitAdmin.declareExchange(deadExchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(deadQueue).to(deadExchange));

        // 发放奖励队列交换机
        DirectExchange exchange = new DirectExchange(RabbitConstants.MQ_EXCHANGE_SEND_AWARD);
        // 声明发送优惠券的消息队列（Direct类型的exchange）
        Queue couponQueue = queue(RabbitConstants.QUEUE_NAME_SEND_COUPON);
        rabbitAdmin.declareQueue(couponQueue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(couponQueue).to(exchange).with(RabbitConstants.MQ_ROUTING_KEY_SEND_COUPON));



        return rabbitAdmin;
    }

    public Queue queue(String name) {
        Map<String, Object> args = new HashMap<>();
        // 设置死信队列
        args.put("x-dead-letter-exchange", RabbitConstants.MQ_EXCHANGE_DEAD_QUEUE);
        args.put("x-dead-letter-routing-key", RabbitConstants.MQ_ROUTING_KEY_DEAD_QUEUE);
        // 设置消息的过期时间， 单位是毫秒
        args.put("x-message-ttl", 5000);

        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(name, durable, exclusive, autoDelete, args);
    }
}