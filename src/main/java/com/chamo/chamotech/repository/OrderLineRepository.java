package com.chamo.chamotech.repository;

import com.chamo.chamotech.entity.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Long> {

    boolean existsByProductId(Long productId);
}
