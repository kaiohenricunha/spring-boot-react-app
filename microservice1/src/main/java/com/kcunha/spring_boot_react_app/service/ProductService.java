package com.kcunha.spring_boot_react_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

import com.kcunha.spring_boot_react_app.model.Product;
import com.kcunha.spring_boot_react_app.model.ProductHistory;
import com.kcunha.spring_boot_react_app.repository.ProductRepository;
import com.kcunha.spring_boot_react_app.repository.ProductHistoryRepository;
import com.kcunha.spring_boot_react_app.dto.InventoryResponse;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private Producer messageSender;

    // Retrieve all products with pagination
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Retrieve product by id
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Stock would be updated based on the response from microservice2 via RabbitMQ
        return product;
    }

    // Create a new product and send RabbitMQ messages
    public Product createProduct(Product product) {
        // Save product in the main app database
        Product savedProduct = productRepository.save(product);

        // Send the new product details to RabbitMQ for other services to consume
        InventoryResponse inventoryRequest = new InventoryResponse(savedProduct.getId(), 0);  // Assuming initial stock is 0
        messageSender.sendInventoryUpdate(inventoryRequest); // Send initial stock information

        return savedProduct;
    }

    // Update a product and send RabbitMQ messages
    public Product updateProduct(Long id, Product updatedProduct, String modifiedBy) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Save history before updating the product
        ProductHistory history = new ProductHistory(
            existingProduct.getId(),
            existingProduct.getName(),
            existingProduct.getDescription(),
            existingProduct.getPrice(),
            modifiedBy,
            LocalDateTime.now()
        );
        productHistoryRepository.save(history);

        // Update product details
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        // Send updated product inventory to RabbitMQ
        InventoryResponse inventoryResponse = new InventoryResponse(existingProduct.getId(), existingProduct.getStock());
        messageSender.sendInventoryUpdate(inventoryResponse);

        return productRepository.save(existingProduct);
    }

    // Delete a product and send a deletion message to RabbitMQ (optional)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(id);

        // Send a message to notify other microservices of the product deletion
        InventoryResponse inventoryResponse = new InventoryResponse(product.getId(), 0); // Assuming stock is reset to 0 on deletion
        messageSender.sendInventoryUpdate(inventoryResponse);
    }

    // Get the history of a product by its ID with pagination
    public Page<ProductHistory> getProductHistory(Long productId, Pageable pageable) {
        return productHistoryRepository.findByProductId(productId, pageable);
    }
}
