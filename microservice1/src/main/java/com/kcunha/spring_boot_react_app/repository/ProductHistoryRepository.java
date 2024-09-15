package com.kcunha.spring_boot_react_app.repository;

import com.kcunha.spring_boot_react_app.model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {

    // Find history by product ID with pagination
    Page<ProductHistory> findByProductId(Long productId, Pageable pageable);
}
