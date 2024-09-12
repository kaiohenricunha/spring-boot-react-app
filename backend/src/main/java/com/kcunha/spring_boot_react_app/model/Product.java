package com.kcunha.spring_boot_react_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Represents a product in the system")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the product", example = "1", required = true)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Schema(description = "Name of the product", example = "Laptop", required = true)
    private String name;

    @Size(max = 255, message = "Description can't be longer than 255 characters")
    @Schema(description = "Description of the product", example = "A high-end gaming laptop")
    private String description;

    @NotNull
    @Min(value = 0, message = "Price must be a positive number")
    @Schema(description = "Price of the product", example = "999.99", required = true)
    private Double price;

    @Version
    @Schema(description = "Version of the product for optimistic locking", example = "1")
    private Long version;

    @Schema(description = "Current stock of the product", example = "50")
    private Integer stock;

    // Constructors
    public Product() {}

    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
