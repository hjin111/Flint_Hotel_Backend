package com.hotel.flint.user.member.service;

import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.dto.MemberSignUpDto;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, EmployeeRepository employeeRepository) {
        this.memberRepository = memberRepository;
        this.employeeRepository = employeeRepository;
    }

    public Member memberSignUp(MemberSignUpDto dto){
        if(memberRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent() ||
                employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent()){
            throw new IllegalArgumentException("해당 이메일로 이미 가입한 계정이 존재합니다.");
        }
        if(memberRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent() ||
                employeeRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent()){
            throw new IllegalArgumentException("해당 번호로 이미 가입한 계정이 존재합니다");
        }
        return memberRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
    }

    public String findEmail(String phoneNumber){
        Member member = memberRepository.findByPhoneNumberAndDelYN(phoneNumber, Option.Y).orElseThrow(
                ()-> new EntityNotFoundException("해당 번호로 가입한 아이디가 없습니다."));
        return member.getEmail();
    }

    public void updatePassword(String email){
        Member member = memberRepository.findByEmailAndDelYN(email, Option.Y).orElseThrow(
                ()-> new EntityNotFoundException("해당 이메일로 가입한 아이디가 없습니다."));

    }

    public Member login(UserLoginDto dto){
        Member member = memberRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).orElseThrow(
                ()-> new EntityNotFoundException("해당 이메일로 가입한 아이디가 없습니다."));
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
