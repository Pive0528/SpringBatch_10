package com.koreait.exam.springbatch_10.app.cart.repository;

import com.koreait.exam.springbatch_10.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByMemberIdAndProductOptionId(Long memberId, Long cartOptionId);
}