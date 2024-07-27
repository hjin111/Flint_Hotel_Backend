package com.hotel.flint.reserve.room.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.reserve.room.service.RoomReservedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
     * 객실 예약 진행
     */
    @PostMapping("/room")
    public ResponseEntity<?> roomReservation(@RequestBody RoomReservedDto dto, @RequestParam Long userId) {

        try {
            double totalPrice = roomReservedService.roomReservation(dto, userId);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약 금액은 " + totalPrice + "원 입니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 객실 예약 취소
     */
    @GetMapping("/room/cancel/{id}")
    public ResponseEntity<?> reservationRoomCanceled(@PathVariable Long id) { // id : room reservation의 id

        try {
            roomReservedService.delete(id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "delete reservation", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 객실 예약 내역 조회 - 목록
     */
    @GetMapping("/room/list")
    public Page<RoomReservedListDto> reservationRoomListCheck(@PageableDefault(size=10, sort = "checkInDate"
            , direction = Sort.Direction.ASC) Pageable pageable, @RequestParam Long userId) {

        return roomReservedService.roomReservedList(pageable, userId);
    }




}