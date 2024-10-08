package com.kcunha.spring_boot_react_app.dto;

import java.io.Serializable;

public class InventoryResponse implements Serializable {

    private Long productId;
    private Integer stock;

    // Constructor
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
}
