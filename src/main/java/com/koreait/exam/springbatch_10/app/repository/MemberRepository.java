package com.koreait.exam.springbatch_10.app.repository;

import com.koreait.exam.springbatch_10.app.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}