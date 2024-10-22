package com.koreait.exam.springbatch_10.app.order.repository;

import com.koreait.exam.springbatch_10.app.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByIdLessThan(long id, Pageable pageable);

    Page<OrderItem> findAllByIdBetween(long fromId, long toId, Pageable pageable);

    Page<OrderItem> findAllByIsPaid(boolean isPaid, Pageable pageable);
}

//10000 건의 데이터
//        701 이하
// 1~700
// 100개씩 끊어서