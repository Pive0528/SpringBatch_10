package com.koreait.exam.springbatch_10.app.member.service;

import com.koreait.exam.springbatch_10.app.cash.entity.CashLog;
import com.koreait.exam.springbatch_10.app.cash.service.CashService;
import com.koreait.exam.springbatch_10.app.member.entity.Member;
import com.koreait.exam.springbatch_10.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final CashService cashService;

    @Transactional
    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email).build();

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public void addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();

        member.setRestCash(newRestCash);

        memberRepository.save(member);
    }

    public long getRestCash(Member member) {

        return member.getRestCash();
    }
}