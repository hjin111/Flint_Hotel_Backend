package com.hotel.flint.reserve.dining.service;

import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.dining.repository.DiningRepository;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.ReservationDeleteDto;
import com.hotel.flint.reserve.dining.dto.ReservationListResDto;
import com.hotel.flint.reserve.dining.dto.ReservationSaveReqDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiningReservationService {

    private final DiningReservationRepository diningReservationRepository;

    private final MemberRepository memberRepository;

    private final DiningRepository diningRepository;

    @Autowired
    public DiningReservationService(DiningReservationRepository diningReservationRepository, MemberRepository memberRepository, DiningRepository diningRepository){
        this.diningReservationRepository = diningReservationRepository;
        this.memberRepository = memberRepository;
        this.diningRepository = diningRepository;
    }

    public void create(ReservationSaveReqDto dto){
        // memberRepository 를 통해 멤버를 찾아오고
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("없는 회원 입니다."));

        // DiningName으로 dining 정보를 가져와서
        Dining dining = diningRepository.findByDiningName(dto.getDiningName()).orElseThrow(() -> new EntityNotFoundException("없는 다이닝 타입입니다."));

        // DiningReservation에 저장
        DiningReservation diningReservation = dto.toEntity(member, dining);
        diningReservationRepository.save(diningReservation);
    }

    // 전체 목록 조회
    public List<ReservationListResDto> list(){

        List<ReservationListResDto> reservationListResDtos = new ArrayList<>();
        List<DiningReservation> diningReservationList = diningReservationRepository.findAll();

        for(DiningReservation reservation : diningReservationList ){
            reservationListResDtos.add( reservation.fromEntity() );
        }

        return reservationListResDtos;
    }

    // 회원별 전체 목록 조회 , 예를 들어 1번 회원이 예약한 목록 전체 조회
    public List<ReservationListResDto> userList(Long userId){

        Member member = memberRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));

        List<ReservationListResDto> reservationListResDtos = new ArrayList<>();
        List<DiningReservation> diningReservationList = diningReservationRepository.findByMemberId(member);

        for(DiningReservation reservation : diningReservationList ){
            reservationListResDtos.add( reservation.fromEntity() );
        }

        return reservationListResDtos;
    }

    public void delete(ReservationDeleteDto dto){

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        DiningReservation diningReservation = new DiningReservation(member, dto);
        diningReservationRepository.delete(diningReservation);

    }

    public void update(ReservationUpdateDto dto){

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        Dining dining = diningRepository.findByDiningName(dto.getDiningName()).orElseThrow(()-> new EntityNotFoundException("없는 다이닝 타입 입니다."));

        DiningReservation diningReservation = new DiningReservation(member, dining, dto);
        diningReservationRepository.save(diningReservation);
    }

}