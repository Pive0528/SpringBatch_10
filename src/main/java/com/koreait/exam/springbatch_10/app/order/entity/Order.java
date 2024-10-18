package com.koreait.exam.springbatch_10.app.order.entity;

import com.koreait.exam.springbatch_10.app.base.entity.BaseEntity;
import com.koreait.exam.springbatch_10.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "product_order")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.calculatePayPrice();
        }

        return payPrice;
    }

    public void setPaymentDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
    }

    public int getPayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public void setRefundDone() {
        for(OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }
    }
}