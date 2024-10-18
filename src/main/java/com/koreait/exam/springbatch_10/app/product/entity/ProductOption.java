package com.koreait.exam.springbatch_10.app.product.entity;

import com.koreait.exam.springbatch_10.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductOption extends BaseEntity {
    private String color;
    private String size;

    private int price; // 권장 판매가 30000
    private int salePrice; // 실제 판매가 40000
    private int wholesalePrice; // 도매가 < 30000
    private int payPrice; // 결제 금액
    private int refundPrice; // 환불 금액
    private int pgFee; // 결제대행사 수수료
    private int refundQuantity; // 환불 한 갯수
    private boolean isPaid; // 결제 여부

    private String displayColor;
    private String displaySize;

    private boolean isSoldout; // 관련 옵션들의 판매불가 여부
    private int stockQuantity; // 보유 물건 갯수

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Product product;

    public ProductOption(String color, String size) {

        this.color = color;
        this.displayColor = color;
        this.size = size;
        this.displaySize = size;
    }

    public boolean isOrderable(int quantity) {
        if (isSoldout() == false) return true;

        return getStockQuantity() >= quantity;
    }
}