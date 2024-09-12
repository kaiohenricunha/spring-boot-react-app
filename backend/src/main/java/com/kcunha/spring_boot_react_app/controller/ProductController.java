package com.kcunha.spring_boot_react_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.kcunha.spring_boot_react_app.model.Product;
import com.kcunha.spring_boot_react_app.model.ProductHistory;
import com.kcunha.spring_boot_react_app.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content)
    })
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productService.getAllProducts(pageable);
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public Product getProductById(
        @Parameter(description = "ID of the product to retrieve", required = true)
        @PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product successfully created", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product data", content = @Content)
    })
    @PostMapping
    public Product createProduct(
        @RequestBody 
        @Schema(description = "Product to be created", required = true) Product product) {
        return productService.createProduct(product);
    }

    @Operation(summary = "Update a product with history tracking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully updated", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{id}")
    public Product updateProduct(
        @Parameter(description = "ID of the product to update", required = true)
        @PathVariable Long id,
        @RequestBody 
        @Schema(description = "Updated product details", required = true) Product updatedProduct,
        @Parameter(description = "Username of the person modifying the product", required = true)
        @RequestParam String modifiedBy) {
        return productService.updateProduct(id, updatedProduct, modifiedBy);
    }

    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(
        @Parameter(description = "ID of the product to delete", required = true)
        @PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @Operation(summary = "Get product history with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product history"),
        @ApiResponse(responseCode = "404", description = "Product history not found", content = @Content)
    })
    @GetMapping("/{id}/history")
    public Page<ProductHistory> getProductHistory(
        @Parameter(description = "ID of the product", required = true)
        @PathVariable Long id, 
        Pageable pageable) {
        return productService.getProductHistory(id, pageable);
    }
}
