package com.hotel.flint.support.qna.repository;

import com.hotel.flint.support.qna.domain.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<QnA, Long> {
}
