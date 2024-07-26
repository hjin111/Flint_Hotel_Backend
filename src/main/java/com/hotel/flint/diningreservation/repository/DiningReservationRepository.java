package com.hotel.flint.diningreservation.repository;

import com.hotel.flint.diningreservation.domain.DiningReservation;
import com.hotel.flint.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningReservationRepository extends JpaRepository<DiningReservation, Long> {

    DiningReservation findByMemberId(Member member);
}
