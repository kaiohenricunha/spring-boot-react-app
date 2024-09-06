package com.kcunha.spring_boot_react_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kcunha.spring_boot_react_app.model.Product;
import com.kcunha.spring_boot_react_app.model.ProductHistory;
import com.kcunha.spring_boot_react_app.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products with pagination
    @GetMapping
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Create a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Update product with history tracking
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, @RequestParam String modifiedBy) {
        return productService.updateProduct(id, updatedProduct, modifiedBy);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // Get product history with pagination
    @GetMapping("/{id}/history")
    public Page<ProductHistory> getProductHistory(@PathVariable Long id, Pageable pageable) {
        return productService.getProductHistory(id, pageable);
    }
}
