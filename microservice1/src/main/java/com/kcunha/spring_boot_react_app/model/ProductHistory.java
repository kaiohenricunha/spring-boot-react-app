package com.kcunha.spring_boot_react_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Schema(description = "Represents the history of a product")
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the history record", example = "1", required = true)
    private Long id;

    @Schema(description = "Product ID associated with this history record", example = "1", required = true)
    private Long productId;

    @Schema(description = "Name of the product at the time of change", example = "Laptop", required = true)
    private String name;

    @Schema(description = "Description of the product at the time of change", example = "A high-end gaming laptop")
    private String description;

    @Schema(description = "Price of the product at the time of change", example = "999.99", required = true)
    private Double price;

    @Schema(description = "User who made the modification", example = "admin", required = true)
    private String modifiedBy;

    @Schema(description = "Date and time when the product was modified", example = "2024-09-11T10:15:30")
    private LocalDateTime modifiedDate;

    // Constructors
    public ProductHistory() {}

    public ProductHistory(Long productId, String name, String description, Double price, String modifiedBy, LocalDateTime modifiedDate) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
