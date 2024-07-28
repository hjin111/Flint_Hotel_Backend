package com.hotel.flint.reserve.dining.controller;

import com.hotel.flint.reserve.dining.dto.ReservationDeleteDto;
import com.hotel.flint.reserve.dining.dto.ReservationSaveReqDto;
import com.hotel.flint.reserve.dining.dto.ReservationListResDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
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

    @PostMapping("/dining/create")
    public String diningReservation(@RequestBody ReservationSaveReqDto dto){
        diningReservationService.create(dto);
        System.out.println(dto);
        return "ok";
    }

    // 예약 전체 목록 조회
    @GetMapping("/dining/list")
    public List<ReservationListResDto> diningReservationList(){
        List<ReservationListResDto> reservationListResDtos = diningReservationService.list();
        return reservationListResDtos;
    }

    // 회원별 예약 전체 목록 조회
    @GetMapping("/dining/list/{userId}")
    public List<ReservationListResDto> diningReservationUserList(@PathVariable Long userId ){
        List<ReservationListResDto> reservationListResDtos = diningReservationService.userList(userId);
        return reservationListResDtos;
    }

    // 예약 삭제/취소
    @DeleteMapping("/dining/delete")
    public String diningDelete(@RequestBody ReservationDeleteDto dto){
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