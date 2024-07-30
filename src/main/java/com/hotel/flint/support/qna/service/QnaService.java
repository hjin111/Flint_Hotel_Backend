package com.hotel.flint.support.qna.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.dto.QnaListDto;
import com.hotel.flint.support.qna.repository.QnaRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * qna 목록 조회
     */
    public Page<QnaListDto> qnaList(Pageable pageable) {

        Page<QnA> list = qnaRepository.findAll(pageable); //qna 리스트 가져오기


        // no구하기
        AtomicInteger start = new AtomicInteger((int) pageable.getOffset());

        Page<QnaListDto> listDtos = list.map(a -> a.listFromEntity((long) start.incrementAndGet()));

        return listDtos;
    }
}
