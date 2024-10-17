package com.koreait.exam.springbatch_10.app.product.entity;

import com.koreait.exam.springbatch_10.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {
    private String name;
    private int price;
    private String makerShopName;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();

    public void addOption(ProductOption option) {
        option.setProduct(this);
        option.setPrice(getPrice());

        productOptions.add(option);
    }
}