package com.hotel.flint.reserve.room.controller;

import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.service.RoomReservedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
public class RoomReservedController {

    private final RoomReservedService roomReservedService;
    @Autowired
    public RoomReservedController (RoomReservedService roomReservedService) {
        this.roomReservedService = roomReservedService;
    }

    /**
     * 룸 예약 진행
     */
    @PostMapping("/room")
    public String roomReservation(@RequestBody RoomReservedDto dto, @RequestParam Long userId) {

        double totalPrice = roomReservedService.roomReservation(dto, userId);

        return "예약 금액은 " + totalPrice + "원 입니다.";
    }


}
