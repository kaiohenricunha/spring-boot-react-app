package com.kcunha.spring_boot_react_app.repository;

import com.kcunha.spring_boot_react_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
