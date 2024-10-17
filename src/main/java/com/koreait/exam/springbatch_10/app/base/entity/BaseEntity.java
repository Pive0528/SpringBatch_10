package com.koreait.exam.springbatch_10.app.base.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Transient // 아래의 필드가 DB 컬럼이 되는 것을 막는다.
    @Builder.Default
    private Map<String, Object> extra = new LinkedHashMap<>();

    public BaseEntity(long id){
        this.id = id;
    }

}