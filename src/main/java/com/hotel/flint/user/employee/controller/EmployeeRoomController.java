package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.service.EmployeeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@RestController
@RequestMapping("/employee/room")
public class EmployeeRoomController {
    private final EmployeeRoomService employeeRoomService;

    @Autowired
    public EmployeeRoomController(EmployeeRoomService employeeRoomService) {
        this.employeeRoomService = employeeRoomService;
    }

    @PatchMapping("/modprice/{room_type_id}")
    public ResponseEntity<?> modRoomPrice(@PathVariable Long room_type_id,
                                          @RequestBody Map<String, Double> request) {
        try {
            employeeRoomService.modRoomPrice(room_type_id, request.get("newPrice"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격 수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> memberReservationRoomCheck(@RequestParam("id") Long id) {
        try {
            InfoRoomResDto infoRoomResDto = employeeRoomService.memberReservationRoomCheck(id);
            return new ResponseEntity<>(infoRoomResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancel_reserve_dining/{id}")
    public ResponseEntity<?> memberReservationCncDiningByEmployee(@PathVariable Long id) {
        try {
            InfoRoomResDto infoRoomResDto = employeeRoomService.memberReservationRoomCheck(id);
            employeeRoomService.memberReservationCncRoomByEmployee(infoRoomResDto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "삭제 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
