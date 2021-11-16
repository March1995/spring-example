package com.wyb.mq.rabbit.constants;

/**
 * RabbitMQ 配置类
 *
 * @author wangyb
 */
public final class RabbitConstants {

    /**
     * 死信队列EXCHANGE名称
     */
    public static final String MQ_EXCHANGE_DEAD_QUEUE = "test-dead-queue-exchange";

    /**
     * 死信队列名称
     */
    public static final String QUEUE_NAME_DEAD_QUEUE = "test-dead-queue";

    /**
     * 死信队列路由名称
     */
    public static final String MQ_ROUTING_KEY_DEAD_QUEUE = "test-routing-key-dead-queue";

    /**
     * 延时队列EXCHANGE名称
     */
    public static final String MQ_EXCHANGE_DELAY_QUEUE = "test-delay-queue-exchange";

    /**
     * 延时队列名称
     */
    public static final String QUEUE_NAME_DELAY_QUEUE = "test-delay-queue";

    /**
     * 延时队列路由名称
     */
    public static final String MQ_ROUTING_KEY_DELAY_QUEUE = "test-routing-key-delay-queue";

    /**
     * 下单后减少库存EXCHANGE名称
     */
    public static final String MQ_EXCHANGE_ORDER_TO_PRODUCT = "order-to-product-exchange";

    /**
     * 下单后减少库存队列名称
     */
    public static final String QUEUE_NAME_ORDER_TO_PRODUCT = "order-to-product-queue";

    /**
     * 下单后减少库存路由key
     */
    public static final String MQ_ROUTING_KEY_ORDER_TO_PRODUCT = "routing-key-order-to-product";

    /**
     * 消息重发的最大次数
     */
    public static final Integer MSG_RETRY_COUNT = 5;

    /**
     * 定时任务时间范围
     */
    public static final Integer TIME_DIFF = 30;

}
