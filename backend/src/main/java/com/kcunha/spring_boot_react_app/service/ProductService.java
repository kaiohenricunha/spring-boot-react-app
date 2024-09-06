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

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    // Retrieve all products with pagination
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Retrieve product by id
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
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
