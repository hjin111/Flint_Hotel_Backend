package com.hotel.flint.member.service;

import com.hotel.flint.common.Option;
import com.hotel.flint.member.domain.Member;
import com.hotel.flint.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String findEmail(String phoneNumber){
        Member member = memberRepository.findByPhoneNumberAndDelYN(phoneNumber, Option.N).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 회원입니다."));
        return member.getEmail();
    }
}
