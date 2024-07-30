package com.hotel.flint.support.qna.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.repository.QnaRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public QnaService (QnaRepository qnaRepository, MemberRepository memberRepository) {
        this.qnaRepository = qnaRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * qna 등록
     */
    @Transactional
    public void createQnA(CreateQnaDto dto) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();//로그인된 이메일 꺼내
        log.info("memberEmail: " + memberEmail);
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 email에 맞는 회원 없음")
        );
        qnaRepository.save(dto.toEntity(member));
    }
}
