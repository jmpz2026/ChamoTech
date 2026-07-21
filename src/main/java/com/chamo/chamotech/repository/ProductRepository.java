package com.chamo.chamotech.repository;

import com.chamo.chamotech.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByCategoryId(Long categoryId);

    @Query("""
            SELECT DISTINCT p FROM ProductEntity p
            LEFT JOIN p.tags t
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
              AND (:priceMin IS NULL OR p.price >= :priceMin)
              AND (:priceMax IS NULL OR p.price <= :priceMax)
              AND (:tag IS NULL OR t.name = :tag)
            """)
    Page<ProductEntity> searchWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("tag") String tag,
            Pageable pageable
    );
}
