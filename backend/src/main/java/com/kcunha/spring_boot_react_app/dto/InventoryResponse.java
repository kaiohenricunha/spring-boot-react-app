package com.kcunha.spring_boot_react_app.dto;

public class InventoryResponse {

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

    // Optional: toString, hashCode, equals methods for better debugging/logging
    @Override
    public String toString() {
        return "InventoryResponse{" +
                "productId=" + productId +
                ", stock=" + stock +
                '}';
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InventoryResponse that = (InventoryResponse) obj;
        return productId.equals(that.productId) && stock.equals(that.stock);
    }
}
