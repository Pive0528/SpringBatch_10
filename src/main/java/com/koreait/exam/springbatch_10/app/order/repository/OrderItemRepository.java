package com.koreait.exam.springbatch_10.app.order.repository;

import com.koreait.exam.springbatch_10.app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}