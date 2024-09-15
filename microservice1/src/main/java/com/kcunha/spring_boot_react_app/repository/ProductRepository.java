package com.kcunha.spring_boot_react_app.repository;

import com.kcunha.spring_boot_react_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom query for retrieving only necessary fields
    @Query("SELECT p.name, p.price FROM Product p WHERE p.name LIKE %:name%")
    List<Object[]> findByNameContaining(@Param("name") String name);

    // Pagination for large product lists
    @SuppressWarnings("null")
    Page<Product> findAll(Pageable pageable);
}
