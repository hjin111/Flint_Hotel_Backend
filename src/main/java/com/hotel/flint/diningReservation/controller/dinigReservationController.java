package com.hotel.flint.diningReservation.controller;

import com.hotel.flint.diningReservation.dto.ReservationReqDto;
import com.hotel.flint.diningReservation.dto.ReservationResDto;
import com.hotel.flint.diningReservation.dto.ReservationUpdateDto;
import com.hotel.flint.diningReservation.service.diningReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class dinigReservationController {

    private final diningReservationService diningReservationService;

    @Autowired
    public dinigReservationController( diningReservationService diningReservationService){
        this.diningReservationService = diningReservationService;
    }

    @PostMapping("/dining/create")
    public String diningReservation(@RequestBody ReservationReqDto dto){
        diningReservationService.create(dto);
        System.out.println(dto);
        return "ok";
    }

    @GetMapping("/dining/list")
    public List<ReservationResDto> diningReservationList(){
        List<ReservationResDto> reservationResDtos = diningReservationService.list();
        return reservationResDtos;
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
