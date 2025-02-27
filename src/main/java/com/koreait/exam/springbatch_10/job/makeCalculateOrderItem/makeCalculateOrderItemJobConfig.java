package com.koreait.exam.springbatch_10.job.makeCalculateOrderItem;

import com.koreait.exam.springbatch_10.app.order.entity.CalculateOrderItem;
import com.koreait.exam.springbatch_10.app.order.entity.OrderItem;
import com.koreait.exam.springbatch_10.app.order.repository.CalculateOrderItemRepository;
import com.koreait.exam.springbatch_10.app.order.repository.OrderItemRepository;
import com.koreait.exam.springbatch_10.util.Util;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
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
//                .start(makeCalculateOrderItemStep0) // 필요시에만 결제 데이터, 정산데이터랑 보정, 일치 시켜주는 작업
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
    @Bean // 2024-02
    public RepositoryItemReader<OrderItem> orderItemReader(
            @Value("#{jobParameters['month']}") String yearMonth
    ) {

        int monthEndDay = Util.date.getEndDayOf(yearMonth);
        LocalDateTime fromDate = Util.date.parse(yearMonth + "-01 00:00:00.000000");
        LocalDateTime toDate = Util.date.parse(yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay));

        return new RepositoryItemReaderBuilder<OrderItem>()
                .name("orderItemReader")
                .repository(orderItemRepository)
                .methodName("findAllByPayDateBetween")
                .pageSize(100)
                .arguments(Arrays.asList(fromDate, toDate))
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

            if (oldCalculateOrderItem != null) {
                calculateOrderItemRepository.delete(oldCalculateOrderItem);
            }

            calculateOrderItemRepository.save(item);
        });
    }


}