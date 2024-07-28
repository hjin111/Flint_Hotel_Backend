package com.hotel.flint.reserve.dining.controller;

import com.hotel.flint.reserve.dining.dto.*;
import com.hotel.flint.reserve.dining.service.DiningReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiningReservationController {

    private final DiningReservationService diningReservationService;

    @Autowired
    public DiningReservationController(DiningReservationService diningReservationService){
        this.diningReservationService = diningReservationService;
    }

    // 다이닝 예약
    @PostMapping("/dining/create")
    public String diningReservation(@RequestBody ReservationSaveReqDto dto){
        diningReservationService.create(dto);
        System.out.println(dto);
        return "ok";
    }

    // 예약 전체 조회
    @GetMapping("/dining/list")
    public List<ReservationListResDto> resevationDiningListCheck(){
        List<ReservationListResDto> reservationListResDtos = diningReservationService.list();
        return reservationListResDtos;
    }

    // 예약 단건 조회
    @GetMapping("/dining/detail/{diningReservationId}")
    public ReservationDetailDto reservationDiningDetailCheck(@PathVariable Long diningReservationId){
        ReservationDetailDto reservationDetailDto = diningReservationService.detailList(diningReservationId);
        return reservationDetailDto;
    }

    // 회원별 예약 전체 목록 조회
    @GetMapping("/dining/list/{userId}")
    public List<ReservationListResDto> reservationDiningUserListCheck(@PathVariable Long userId ){
        List<ReservationListResDto> reservationListResDtos = diningReservationService.userList(userId);
        return reservationListResDtos;
    }

    // 예약 삭제/취소
    @DeleteMapping("/dining/delete")
    public String reservationDiningCanceled(@RequestBody ReservationDeleteDto dto){
        diningReservationService.delete(dto);
        return "예약 취소";
    }

    // 예약 수정
    @PostMapping("/dining/update")
    public String diningUpdate(@RequestBody ReservationUpdateDto dto){
        diningReservationService.update(dto);
        return "예약 수정 완료";
    }

}