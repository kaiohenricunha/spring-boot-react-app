package com.kcunha.spring_boot_react_app;

import com.kcunha.spring_boot_react_app.dto.InventoryResponse;
import com.kcunha.spring_boot_react_app.model.Product;
import com.kcunha.spring_boot_react_app.model.ProductHistory;
import com.kcunha.spring_boot_react_app.repository.ProductRepository;
import com.kcunha.spring_boot_react_app.repository.ProductHistoryRepository;
import com.kcunha.spring_boot_react_app.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductHistoryRepository productHistoryRepository;

    @MockBean
    private RestTemplate restTemplate;

    private Product product;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        product = new Product("Test Product", "Test Description", 99.99);
        product.setId(1L);
        pageable = PageRequest.of(0, 10); // Example Pageable object
    }

    // Test creating a product
    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product createdProduct = productService.createProduct(product);
        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    // Test retrieving a product by ID and mocking inventory service
    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Mock RestTemplate call to Inventory Service
        InventoryResponse mockInventoryResponse = new InventoryResponse(1L, 50);
        when(restTemplate.getForObject("http://inventory-service:8081/api/inventory/1", InventoryResponse.class))
            .thenReturn(mockInventoryResponse);

        Product foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals(50, foundProduct.getStock()); // Verify that the stock was set
        verify(productRepository, times(1)).findById(1L);
        verify(restTemplate, times(1)).getForObject("http://inventory-service:8081/api/inventory/1", InventoryResponse.class);
    }

    // Test updating a product and saving history
    @Test
    public void testUpdateProduct() {
        Product updatedProduct = new Product("Updated Product", "Updated Description", 199.99);
        updatedProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);  // Match any Product instance

        Product result = productService.updateProduct(1L, updatedProduct, "admin");

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        
        // Verify that the history was saved with correct arguments
        verify(productHistoryRepository, times(1)).save(any(ProductHistory.class));
        verify(productRepository, times(1)).save(any(Product.class));  // Use argument matcher for Product
    }

    // Test deleting a product
    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    // Test retrieving product history
    @Test
    public void testGetProductHistory() {
        List<ProductHistory> history = new ArrayList<>();
        ProductHistory record = new ProductHistory(1L, "Test Product", "Test Description", 99.99, "admin", null);
        history.add(record);
        Page<ProductHistory> historyPage = new PageImpl<>(history, pageable, history.size());

        when(productHistoryRepository.findByProductId(1L, pageable)).thenReturn(historyPage);

        Page<ProductHistory> result = productService.getProductHistory(1L, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
        verify(productHistoryRepository, times(1)).findByProductId(1L, pageable);
    }
}
