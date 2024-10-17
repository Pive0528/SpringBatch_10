
package com.koreait.exam.springbatch_10.app.member.repository;

import com.koreait.exam.springbatch_10.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
