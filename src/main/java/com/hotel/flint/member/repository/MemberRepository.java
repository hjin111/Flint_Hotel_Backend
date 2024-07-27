package com.hotel.flint.member.repository;

import com.hotel.flint.common.Option;
import com.hotel.flint.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDelYN(String email, Option delYN);
    Optional<Member> findByPhoneNumberAndDelYN(String phoneNumber, Option delYN);
}
