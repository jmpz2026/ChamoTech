package com.chamo.chamotech.repository;

import com.chamo.chamotech.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByCategoryId(Long categoryId);

    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);
}
