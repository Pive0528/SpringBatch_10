package com.koreait.exam.springbatch_10.app.order.entity;

import com.koreait.exam.springbatch_10.app.base.entity.BaseEntity;
import com.koreait.exam.springbatch_10.app.product.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CalculateOrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductOption productOption;

    private int quantity;

    // 가격
    private int price; // 권장 판매가 30000
    private int salePrice; // 실제 판매가 40000
    private int wholesalePrice; // 도매가 < 30000
    private int payPrice; // 결제 금액
    private int refundPrice; // 환불 금액
    private int pgFee; // 결제대행사 수수료
    private int refundQuantity; // 환불 한 갯수
    private boolean isPaid; // 결제 여부

    // 상품
    private String productName;

    // 상품 옵션
    private String productOptionColor;
    private String productOptionSize;
    private String productOptionDisplayColor;
    private String productOptionDisplaySize;

    public CalculateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        order = orderItem.getOrder();
        productOption = orderItem.getProductOption();
        quantity = orderItem.getQuantity();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        pgFee = orderItem.getPgFee();
        refundQuantity = orderItem.getRefundQuantity();
        isPaid = orderItem.isPaid();

        //상품
        productName = orderItem.getProductOption().getProduct().getName();
        //상품 옵션
        productOptionColor = orderItem.getProductOption().getColor();
        productOptionSize = orderItem.getProductOption().getSize();
        productOptionDisplayColor = orderItem.getProductOption().getDisplayColor();
        productOptionDisplaySize = orderItem.getProductOption().getDisplaySize();


    }
}