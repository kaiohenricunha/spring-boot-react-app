package com.kcunha.spring_boot_react_app.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "inventory_queue";
    public static final String EXCHANGE_NAME = "inventory_exchange";
    public static final String ROUTING_KEY = "inventory_routing_key";

    @Bean
    public Queue inventoryQueue() {
        return new Queue(QUEUE_NAME, true);  // Declare the queue as durable
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryQueue).to(inventoryExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
