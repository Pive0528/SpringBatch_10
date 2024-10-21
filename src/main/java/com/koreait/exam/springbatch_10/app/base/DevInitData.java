package com.koreait.exam.springbatch_10.app.base;

import com.koreait.exam.springbatch_10.app.cart.service.CartService;
import com.koreait.exam.springbatch_10.app.member.entity.Member;
import com.koreait.exam.springbatch_10.app.member.service.MemberService;
import com.koreait.exam.springbatch_10.app.order.entity.Order;
import com.koreait.exam.springbatch_10.app.order.service.OrderService;
import com.koreait.exam.springbatch_10.app.product.entity.Product;
import com.koreait.exam.springbatch_10.app.product.entity.ProductOption;
import com.koreait.exam.springbatch_10.app.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("dev")
@Slf4j
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService, CartService cartService,
                                      OrderService orderService) {
        return args -> {

            class Helper {
                public Order order(Member member, List<ProductOption> productOptions) {
                    for (int i = 0; i < productOptions.size(); i++) {
                        ProductOption productOption = productOptions.get(i);

                        cartService.addItem(member, productOption, i + 1);
                    }
                    return orderService.createFromCart(member);
                }
            }

            Helper helper = new Helper();

            String password = "{noop}1234";
            Member member1 = memberService.join("user1", password, "user1@test.com");
            Member member2 = memberService.join("user2", password, "user2@test.com");
            Member member3 = memberService.join("user3", password, "user3@test.com");
            Member member4 = memberService.join("user4", password, "user4@test.com");

            // 만원 충전
            memberService.addCash(member1, 10_000, "충전_무통장입금");
            // 2만원 충전
            memberService.addCash(member1, 20_000, "충전_무통장입금");
            // 5천원 사용
            memberService.addCash(member1, -5_000, "출금_일반");

            memberService.addCash(member1, 10_000_000, "충전_무통장입금");

            long restCash = memberService.getRestCash(member1);

            log.info("member1 rest cash: " + restCash);

            Product product1 = productService.create("반팔 1", 55000, 45000, "DDM-1",
                    Arrays.asList(new ProductOption("RED", "95"),
                            new ProductOption("RED", "100"),
                            new ProductOption("BLUE", "95"),
                            new ProductOption("BLUE", "100")));
            Product product2 = productService.create("셔츠 1", 66000, 40000, "DDM-2",
                    Arrays.asList(new ProductOption("WHITE", "95"),
                            new ProductOption("WHITE", "100"),
                            new ProductOption("BLACK", "95"),
                            new ProductOption("BLACK", "100")));

            ProductOption product1Option__RED_95 = product1.getProductOptions().get(0);
            ProductOption product1Option__RED_100 = product1.getProductOptions().get(1);
            ProductOption product1Option__BLUE_95 = product1.getProductOptions().get(2);
            ProductOption product1Option__BLUE_100 = product1.getProductOptions().get(3);

            Order order1 = helper.order(member1, Arrays.asList(product1Option__RED_95, product1Option__RED_95, product1Option__BLUE_95));

            int order1PayPrice = order1.calculatePayPrice();
            log.info("order1 pay price: " + order1PayPrice);

            orderService.payByRestCashOnly(order1);

            // 회원 2, 주문 2

            ProductOption product2Option__WHITE_95 = product2.getProductOptions().get(0);
            ProductOption product2Option__WHITE_100 = product2.getProductOptions().get(1);
            ProductOption product2Option__BLACK_95 = product2.getProductOptions().get(2);
            ProductOption product2Option__BLACK_100 = product2.getProductOptions().get(3);


            Order order2 = helper.order(member2, Arrays.asList(product2Option__BLACK_95, product2Option__BLACK_95, product2Option__WHITE_95));

            int order2PayPrice = order2.calculatePayPrice();
            log.info("order2 pay price: " + order2PayPrice);

            // 37만원 충전
            memberService.addCash(member2, 1_000_000_000, "충전_무통장입금");

            log.info("member2 restCash: " + member2.getRestCash());

            orderService.payByRestCashOnly(order2);

            orderService.refund(order2);

            Order order3 = helper.order(member2, Arrays.asList(product1Option__RED_95, product1Option__RED_100, product2Option__BLACK_95, product2Option__WHITE_95));

            orderService.payByRestCashOnly(order3);

            Order order4 = helper.order(member1, Arrays.asList(product1Option__RED_95, product2Option__WHITE_95));

            orderService.payByRestCashOnly(order4);
        };
    }
}