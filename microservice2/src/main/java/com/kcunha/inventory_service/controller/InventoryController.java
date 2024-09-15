package com.kcunha.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kcunha.inventory_service.model.Inventory;
import com.kcunha.inventory_service.service.InventoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Get all inventory items
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryList);
    }

    // Get inventory by product ID
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long productId) {
        Optional<Inventory> inventory = inventoryService.getInventoryByProductId(productId);
        return inventory.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // Add new inventory item
    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        Inventory savedInventory = inventoryService.addInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInventory);
    }

    // Update inventory stock
    @PostMapping("/{productId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long productId, @RequestParam Integer stock) {
        Inventory updatedInventory = inventoryService.updateInventory(productId, stock);
        return ResponseEntity.ok(updatedInventory);
    }

    // Decrease stock (e.g., after a sale)
    @PostMapping("/{productId}/decrease")
    public ResponseEntity<Inventory> decreaseStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        Inventory updatedInventory = inventoryService.decreaseStock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }
}
