package com.hotel.flint.support.qna.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.dto.QnaDetailDto;
import com.hotel.flint.support.qna.dto.QnaListDto;
import com.hotel.flint.support.qna.dto.QnaUpdateDto;
import com.hotel.flint.support.qna.repository.QnaRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
     * qna 목록 조회 - 마이페이지에서 본인 것만
     */
    public List<QnaListDto> qnaList(Pageable pageable) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 email의 회원 없음")
        );

        Page<QnA> list = qnaRepository.findByMember(pageable, member); //qna 리스트 가져오기

        AtomicInteger start = new AtomicInteger((int) pageable.getOffset() + 1);

        List<QnaListDto> listDtos = list.stream()
                .map(a -> a.listFromEntity((long) start.getAndIncrement()))
                .collect(Collectors.toList());

        return listDtos;
    }

    /**
     * qna 단건 조회 - 상세조회
     */
    public QnaDetailDto qnaDetail(Long id) {

        // 로그인된 회원 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );

        return qna.detailFromEntity(memberEmail);
    }

    /**
     * qna 수정
     */
    @Transactional
    public void qnaUpdate(Long id, QnaUpdateDto dto) {

        // 로그인된 회원 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );

        qna.updateFromEntity(dto);
    }

    /**
     * qna 삭제
     */
    @Transactional
    public void delete(Long id) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );
        qnaRepository.delete(qna);
    }

}
