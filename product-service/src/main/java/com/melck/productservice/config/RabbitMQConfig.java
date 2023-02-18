package com.melck.productservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public Queue queue() {
        return new Queue("");
    }
}
