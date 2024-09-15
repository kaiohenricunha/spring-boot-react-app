package com.kcunha.inventory_service.dto;

import java.io.Serializable;

public class InventoryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private Integer stock;

    // Default constructor
    public InventoryResponse() {}

    // Constructor with parameters
    public InventoryResponse(Long productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "InventoryResponse{" +
                "productId=" + productId +
                ", stock=" + stock +
                '}';
    }
}
