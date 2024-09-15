package com.kcunha.inventory_service.service;

import com.kcunha.inventory_service.model.Inventory;
import com.kcunha.inventory_service.repository.InventoryRepository;
import com.kcunha.inventory_service.config.RabbitMQConfig;
import com.kcunha.inventory_service.dto.InventoryResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    // Retrieve all inventory items
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    // Retrieve inventory by product ID
    public Optional<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    // Add new inventory item
    public Inventory addInventory(Inventory inventory) {
        Optional<Inventory> existingInventory = inventoryRepository.findByProductId(inventory.getProductId());
        if (existingInventory.isPresent()) {
            throw new RuntimeException("Inventory for product already exists");
        }
        return inventoryRepository.save(inventory);
    }

    // Decrease inventory stock
    public Inventory decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (inventory.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }
        inventory.setStock(inventory.getStock() - quantity);
        return inventoryRepository.save(inventory);
    }

    // Receive inventory update via RabbitMQ
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveInventoryUpdate(InventoryResponse inventoryResponse) {
        if (inventoryResponse.getProductId() == null || inventoryResponse.getStock() == null) {
            logger.error("Failed to process inventory update: {}", inventoryResponse);
            return;
        }

        System.out.println("Received inventory update for product: " + inventoryResponse.getProductId());

        // Update the inventory stock
        updateInventory(inventoryResponse.getProductId(), inventoryResponse.getStock());
    }

    // Update inventory stock
    public Inventory updateInventory(Long productId, Integer stock) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> new Inventory(productId, stock)); // Create new inventory if not found
        inventory.setStock(stock);
        return inventoryRepository.save(inventory);
    }
}
