package com.kcunha.inventory_service.service;

import com.kcunha.inventory_service.model.Inventory;
import com.kcunha.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

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

    // Update inventory stock
    public Inventory updateInventory(Long productId, Integer stock) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(new Inventory(productId, stock)); // Create new inventory if not found
        inventory.setStock(stock);
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
}
