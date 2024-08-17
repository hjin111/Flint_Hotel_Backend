package com.hotel.flint.reserve.dining.service;

import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.dining.repository.DiningRepository;
import com.hotel.flint.reserve.dining.controller.DiningSseController;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.*;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiningReservationService {

    private final DiningReservationRepository diningReservationRepository;

    private final MemberRepository memberRepository;

    private final DiningRepository diningRepository;

    private final EmployeeRepository employeeRepository;
    private final DiningSseController diningSseController;

    @Autowired
    public DiningReservationService(DiningReservationRepository diningReservationRepository, MemberRepository memberRepository, DiningRepository diningRepository, EmployeeRepository employeeRepository, DiningSseController diningSseController){
        this.diningReservationRepository = diningReservationRepository;
        this.memberRepository = memberRepository;
        this.diningRepository = diningRepository;
        this.employeeRepository = employeeRepository;
        this.diningSseController = diningSseController;
    }


    private Employee getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String email = userDetails.getUsername();
            return employeeRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }

    private Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String email = userDetails.getUsername();
            return memberRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }



    // 다이닝 예약
    public DiningReservation create(ReservationSaveReqDto dto){
        // 멤버 찾아오고
        Member member = getAuthenticatedMember();

        // DiningName으로 dining 정보를 가져와서
        Dining dining = diningRepository.findById(dto.getDiningId()).orElseThrow(() -> new EntityNotFoundException("없는 다이닝 타입입니다."));

        // DiningReservation에 저장
        DiningReservation diningReservation = dto.toEntity(member, dining);
        DiningName diningName = diningReservation.getDiningId().getDiningName();

        String email = "flint_" + diningName.toString().substring(0,3) + "@gmail.com";

        System.out.println(email);
        ReservationSseDetailDto reservationSseDetailDto = diningReservation.fromSseEntity(dto.getDiningId());

        diningSseController.publishMessage(reservationSseDetailDto, email);
        return diningReservationRepository.save(diningReservation);
    }

    // 예약 전체 조회 - 관리자( 같은 부서인 예약 내역들만 볼 수 있음 )
//    public List<ReservationListResDto> list(){
//
//        Employee employee = getAuthenticatedEmployee();
//
//        List<ReservationListResDto> reservationListResDtos = new ArrayList<>();
//        List<DiningReservation> diningReservationList = diningReservationRepository.findAll();
//
//        for(DiningReservation reservation : diningReservationList ){
//
//            if( employee.getDepartment().toString().equals(reservation.getDiningId().getDiningName().toString()) ){
//                reservationListResDtos.add( reservation.fromEntity() );
//            }
//
//        }
//
//        return reservationListResDtos;
//    }

    public Page<ReservationListResDto> list(Pageable pageable){

        Employee employee = getAuthenticatedEmployee();

        Page<DiningReservation> diningReservationList = diningReservationRepository.findAll(pageable);
        List<ReservationListResDto> reservationListResDtos = diningReservationList
                .stream()
                .filter(reservation -> employee.getDepartment().toString().equals(reservation.getDiningId().getDiningName().toString()))
                .map(a -> a.fromListEntity())
                .collect(Collectors.toList());

        System.out.println(reservationListResDtos);

        return new PageImpl<>(reservationListResDtos, pageable, diningReservationList.getTotalElements());

    }

    // 예약 단건 조회
    public ReservationDetailDto detailList(Long diningReservationId){

        Member member = getAuthenticatedMember();

        DiningReservation dto = diningReservationRepository.findById(diningReservationId)
                .orElseThrow(() -> new EntityNotFoundException("예약 내역이 없습니다."));
        if(member.equals(dto.getMemberId())){
            ReservationDetailDto reservationDetailDto = dto.fromEntity(diningReservationId);
            return reservationDetailDto;
        }else{
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    // 회원별 전체 목록 조회 , 예를 들어 1번 회원이 예약한 목록 전체 조회
    public List<ReservationListResDto> userList(){

        Member member = getAuthenticatedMember();

        List<ReservationListResDto> reservationListResDtos = new ArrayList<>();
        List<DiningReservation> diningReservationList = diningReservationRepository.findByMemberId(member);

        for(DiningReservation reservation : diningReservationList ){
            reservationListResDtos.add( reservation.fromListEntity() );
        }

        return reservationListResDtos;
    }

    // 예약 취소
    public void delete(Long id){

        Member member = getAuthenticatedMember();

        DiningReservation diningReservation = new DiningReservation(member, id);
        diningReservationRepository.delete(diningReservation);

    }

    // 예약 수정 - 관리자
    public DiningReservation update(Long id, ReservationUpdateDto dto){

        Employee employee = getAuthenticatedEmployee();

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        Dining dining = diningRepository.findByDiningName(dto.getDiningName()).orElseThrow(()-> new EntityNotFoundException("없는 다이닝 타입 입니다."));

        if(employee.getDepartment().toString().equals(dto.getDiningName().toString())) {
            DiningReservation diningReservation = new DiningReservation(id, member, dining, dto);
            diningReservationRepository.save(diningReservation);
            return diningReservation;
        }else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

    }

}