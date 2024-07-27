package com.hotel.flint.reserve.dining.controller;

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

    @GetMapping("/dining/list")
    public List<ReservationListResDto> diningReservationList(){
        List<ReservationListResDto> reservationListResDtos = diningReservationService.list();
        return reservationListResDtos;
    }

    @DeleteMapping("/dining/delete/{id}")
    public String diningDelete(@PathVariable Long id){
        diningReservationService.delete(id);
        return "예약 취소";
    }

    @PostMapping("/dining/update/{id}")
    public String diningUpdate(@PathVariable Long id, @RequestBody ReservationUpdateDto dto){
        diningReservationService.update(id, dto);
        return "예약 수정 완료";
    }

}