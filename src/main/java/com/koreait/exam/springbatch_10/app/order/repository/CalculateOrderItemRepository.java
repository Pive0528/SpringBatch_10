package com.koreait.exam.springbatch_10.app.order.repository;

import com.koreait.exam.springbatch_10.app.order.entity.CalculateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculateOrderItemRepository extends JpaRepository<CalculateOrderItem, Long> {
    Optional<CalculateOrderItem> findByOrderItemId(Long orderItemId);
}