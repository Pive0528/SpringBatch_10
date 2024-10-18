package com.koreait.exam.springbatch_10.app.order.entity;

import com.koreait.exam.springbatch_10.app.base.entity.BaseEntity;
import com.koreait.exam.springbatch_10.app.product.entity.Product;
import com.koreait.exam.springbatch_10.app.product.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = LAZY)
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

    public OrderItem(ProductOption productOption, int quantity) {
        this.productOption = productOption;
        this.quantity = quantity;
        this.price = productOption.getPrice();
        this.salePrice = productOption.getSalePrice();
        this.wholesalePrice = productOption.getWholesalePrice();
    }

    public int calculatePayPrice() {
        return salePrice * quantity;
    }

    public void setPaymentDone() {
        this.payPrice = calculatePayPrice();
        this.isPaid = true;
    }

    public void setRefundDone() {
        if (refundQuantity == quantity) return;

        this.refundQuantity = quantity;
        this.refundPrice = payPrice;
    }
}