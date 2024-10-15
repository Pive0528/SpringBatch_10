package com.koreait.exam.springbatch_10;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatch10Application {
    public static void main(String[] args) {

        SpringApplication.run(SpringBatch10Application.class, args);
    }
}