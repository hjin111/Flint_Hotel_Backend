package com.hotel.flint.reserve.dining.repository;

import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.user.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningReservationRepository extends JpaRepository<DiningReservation, Long> {
    List<DiningReservation> findByMemberId(Member member);
}