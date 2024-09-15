package com.kcunha.spring_boot_react_app.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcunha.spring_boot_react_app.config.RabbitMQConfig;
import com.kcunha.spring_boot_react_app.dto.InventoryResponse;
import com.kcunha.spring_boot_react_app.model.Product;

@Service
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Method to update inventory and send the update message to RabbitMQ
    public void sendInventoryUpdate(InventoryResponse inventoryResponse) {
        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, inventoryResponse);
        System.out.println("Sent inventory update for product: " + inventoryResponse.getProductId());
    }

    // Send product creation details to the queue (optional, based on your logic)
    public void sendProductCreated(Product product) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, product);
    }
}
