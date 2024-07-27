package com.hotel.flint.user.member.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
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
                ()-> new EntityNotFoundException("해당 번호로 가입한 아이디가 없습니다."));
        return member.getEmail();
    }

    public void updatePassword(String email){
        Member member = memberRepository.findByEmailAndDelYN(email, Option.N).orElseThrow(
                ()-> new EntityNotFoundException("해당 이메일로 가입한 아이디가 없습니다."));

    }
}
