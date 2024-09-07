package com.kcunha.spring_boot_react_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;    

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

    // Get the inventory for a product from Inventory Service
    private Integer getInventoryByProductId(Long productId) {
        String url = inventoryServiceUrl + "/" + productId;
        InventoryResponse response = restTemplate.getForObject(url, InventoryResponse.class);
        return response != null ? response.getStock() : 0;
    }

    // Create a new product
    public Product createProduct(Product product) {
        return productRepository.save(product);
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
