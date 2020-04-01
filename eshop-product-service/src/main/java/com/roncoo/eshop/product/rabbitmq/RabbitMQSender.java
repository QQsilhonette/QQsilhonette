package com.roncoo.eshop.product.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/1 10:24 上午
 * @description：mq生产者
 */

@Component
public class RabbitMQSender {

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(String queue, String message) {
        this.rabbitTemplate.convertAndSend(queue, message);
    }
}
