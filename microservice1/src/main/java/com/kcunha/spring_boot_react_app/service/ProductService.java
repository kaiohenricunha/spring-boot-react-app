package com.kcunha.spring_boot_react_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import com.kcunha.spring_boot_react_app.model.Product;
import com.kcunha.spring_boot_react_app.model.ProductHistory;
import com.kcunha.spring_boot_react_app.repository.ProductRepository;
import com.kcunha.spring_boot_react_app.repository.ProductHistoryRepository;
import com.kcunha.spring_boot_react_app.dto.InventoryResponse;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    private String inventoryServiceName = "http://inventory-service/api/inventory"; // Using the Eureka service name

    // Retrieve all products with pagination
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Retrieve product by id with inventory info
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Fetch inventory info from Inventory Service
        Integer stock = getInventoryByProductId(product.getId());
        product.setStock(stock); // Assuming `Product` has a `stock` field

        return product;
    }

    // Create a new product
    public Product createProduct(Product product) {
        // Save product in the main app database
        Product savedProduct = productRepository.save(product);

        // Initialize stock in the inventory-service
        createInventoryForProduct(savedProduct.getId(), 0);  // Set initial stock to 0

        return savedProduct;
    }

    // Get the inventory for a product from Inventory Service
    private Integer getInventoryByProductId(Long productId) {
        String url = inventoryServiceName + "/" + productId;
        InventoryResponse response = restTemplate.getForObject(url, InventoryResponse.class);
        return response != null ? response.getStock() : 0;
    }

    // Create inventory in the inventory-service for the newly created product
    private void createInventoryForProduct(Long productId, Integer initialStock) {
        String url = inventoryServiceName; // points to http://inventory-service/api/inventory
        InventoryResponse inventoryRequest = new InventoryResponse(productId, initialStock);
    
        // Make a POST request to inventory-service to create inventory
        restTemplate.postForObject(url, inventoryRequest, InventoryResponse.class);
    }    

    // Update a product and save history
    public Product updateProduct(Long id, Product updatedProduct, String modifiedBy) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Create a history record before updating
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

        return productRepository.save(existingProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get the history of a product by its ID with pagination
    public Page<ProductHistory> getProductHistory(Long productId, Pageable pageable) {
        return productHistoryRepository.findByProductId(productId, pageable);
    }
}
