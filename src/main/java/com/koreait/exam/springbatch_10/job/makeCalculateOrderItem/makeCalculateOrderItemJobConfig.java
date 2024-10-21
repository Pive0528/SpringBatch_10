package com.koreait.exam.springbatch_10.job.makeCalculateOrderItem;

import com.koreait.exam.springbatch_10.app.order.entity.CalculateOrderItem;
import com.koreait.exam.springbatch_10.app.order.entity.OrderItem;
import com.koreait.exam.springbatch_10.app.order.repository.CalculateOrderItemRepository;
import com.koreait.exam.springbatch_10.app.order.repository.OrderItemRepository;
import com.koreait.exam.springbatch_10.app.product.entity.Product;
import com.koreait.exam.springbatch_10.app.product.entity.ProductBackup;
import com.koreait.exam.springbatch_10.app.product.repository.ProductBackupRepository;
import com.koreait.exam.springbatch_10.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class makeCalculateOrderItemJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OrderItemRepository orderItemRepository;
    private final CalculateOrderItemRepository calculateOrderItemRepository;

    @Bean
    public Job makeCalculateOrderItemJob(Step makeCalculateOrderItemStep1, CommandLineRunner initData) throws Exception {

        initData.run();

        return jobBuilderFactory.get("makeCalculateOrderItemJob")
                .start(makeCalculateOrderItemStep1)
                .build();
    }

    @Bean
    @JobScope
    public Step makeCalculateOrderItemStep1(
            ItemReader orderItemReader,
            ItemProcessor orderItemToCalculateOrderItemProcessor,
            ItemWriter calculateOrderItemWriter
    ) {
        return stepBuilderFactory.get("makeCalculateOrderItemStep1")
                .<OrderItem, CalculateOrderItem>chunk(100)
                .reader(orderItemReader)
                .processor(orderItemToCalculateOrderItemProcessor)
                .writer(calculateOrderItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<OrderItem> orderItemReader() {
        return new RepositoryItemReaderBuilder<OrderItem>()
                .name("orderItemReader")
                .repository(orderItemRepository)
                .methodName("findAllByIdLessThan")
                .pageSize(100)
                .arguments(Arrays.asList(6L))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<OrderItem, CalculateOrderItem> orderItemToCalculateOrderItemProcessor() {
        return orderItem -> new CalculateOrderItem(orderItem);
    }

    @StepScope
    @Bean
    public ItemWriter<CalculateOrderItem> calculateOrderItemWriter() {
        return items -> items.forEach(item -> {
            CalculateOrderItem oldCalculateOrderItem = calculateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);

            if(oldCalculateOrderItem != null) {
                calculateOrderItemRepository.delete(oldCalculateOrderItem);
            }

            calculateOrderItemRepository.save(item);
        });
    }


}