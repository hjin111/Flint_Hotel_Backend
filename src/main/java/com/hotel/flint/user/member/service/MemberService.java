package com.hotel.flint.user.member.service;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.dto.MemberDetResDto;
import com.hotel.flint.user.member.dto.MemberModResDto;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    멤버 상세정보 로직
    public MemberDetResDto memberDetail(Long id){
        Member member = findByUserId(id);

        return member.detUserEntity();
    }

//    멤버 삭제 로직 delyn N -> Y
    public void memberDelete(Long id){
        Member member = memberRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 id가 존재하지 않습니다."));
        member.deleteUser();
        memberRepository.save(member);
    }
    
//    멤버 비밀번호 수정 로직
    public void memberModify(MemberModResDto dto){
        Member member = this.findByUserId(dto.getId());
        member.modifyUser(dto.getPassword());
        memberRepository.save(member);
    }
    /*
    * 멤버 id로 member 객체 찾는 로직
    * */
    public Member findByUserId(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 id가 존재하지 않습니다."));
        return member;
    }
}