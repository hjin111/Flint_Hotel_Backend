package com.hotel.flint.support.qna.repository;

import com.hotel.flint.support.qna.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<QnA, Long> {

    // 페이징 처리
    Page<QnA> findAll(Pageable pageable);
}
