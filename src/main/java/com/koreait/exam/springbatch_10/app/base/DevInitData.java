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

@Configuration
@Profile("dev")
@Slf4j
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService, CartService cartService,
                                      OrderService orderService) {
        return args -> {
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

            // 30만원 충전
            memberService.addCash(member1, 300_000, "충전_무통장입금");

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
            ProductOption product1Option__BLUE_95 = product1.getProductOptions().get(2);

            // 회원 1, 주문 1
            cartService.addItem(member1, product1Option__RED_95, 1); // productOption__RED_95  총 수량 1
            cartService.addItem(member1, product1Option__RED_95, 2); // productOption__RED_95 총 수량 3
            cartService.addItem(member1, product1Option__BLUE_95, 1); //productOption__BLUE_95  총 수량 1

            Order order1 = orderService.createFromCart(member1);

            int order1PayPrice = order1.calculatePayPrice();
            log.info("order1 pay price: " + order1PayPrice);

            orderService.payByRestCashOnly(order1);

            // 회원 2, 주문 2

            ProductOption product2Option__BLACK_95 = product2.getProductOptions().get(2);
            ProductOption product2Option__WHITE_95 = product2.getProductOptions().get(0);

            cartService.addItem(member2, product2Option__BLACK_95, 1); // product2Option__BLACK_95  총 수량 1
            cartService.addItem(member2, product2Option__BLACK_95, 2); // product2Option__BLACK_95 총 수량 3
            cartService.addItem(member2, product2Option__WHITE_95, 1); //product2Option__WHITE_95  총 수량 1

            Order order2 = orderService.createFromCart(member2);

            int order2PayPrice = order2.calculatePayPrice();
            log.info("order2 pay price: " + order2PayPrice);

            // 37만원 충전
            memberService.addCash(member2, 370_000, "충전_무통장입금");

            log.info("member2 restCash: " + member2.getRestCash());

            orderService.payByRestCashOnly(order2);

            orderService.refund(order2);
        };
    }
}