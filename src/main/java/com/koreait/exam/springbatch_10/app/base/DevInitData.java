package com.koreait.exam.springbatch_10.app.base;

import com.koreait.exam.springbatch_10.app.member.entity.Member;
import com.koreait.exam.springbatch_10.app.member.service.MemberService;
import com.koreait.exam.springbatch_10.app.product.entity.ProductOption;
import com.koreait.exam.springbatch_10.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("dev")
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService){
        return args -> {
            String password = "{noop}1234";
            Member member1 = memberService.join("user1",password,"user1@test.com");
            Member member2 = memberService.join("user2",password,"user2@test.com");
            Member member3 = memberService.join("user3",password,"user3@test.com");
            Member member4 = memberService.join("user4",password,"user4@test.com");

            productService.create("반팔 1",55000,"DDM-1",
                    Arrays.asList(new ProductOption("RED","95"),
                            new ProductOption("RED","100"),
                            new ProductOption("BLUE","95"),
                            new ProductOption("BLUE","100")));
            productService.create("셔츠 1",66000,"DDM-2",
                    Arrays.asList(new ProductOption("WHITE","95"),
                            new ProductOption("WHITE","100"),
                            new ProductOption("BLACK","95"),
                            new ProductOption("BLACK","100")));
        };
    }
}