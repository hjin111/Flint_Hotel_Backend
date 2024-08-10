package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.user.employee.dto.EmployeeModRoomDto;
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
                                          @RequestBody Map<String, Long> request) {
        try {
            employeeRoomService.modRoomPrice(room_type_id, request.get("newPrice"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격 수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> memberReservationRoomCheck(@RequestParam("id") Long id) {
        try {
            InfoRoomResDto dto = employeeRoomService.memberReservationRoomCheck(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/cancel_reserve_room")
    public ResponseEntity<?> memberReservationCncRoomByEmployee(@RequestParam Long id) {
        try {
            InfoRoomResDto infoRoomResDto = employeeRoomService.memberReservationRoomCheck(id);
            employeeRoomService.memberReservationCncRoomByEmployee(infoRoomResDto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "삭제 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 고객의 요청시, 객실 예약내역 수정
     */
    @PostMapping("/modify/{id}") // reserve_id
    public ResponseEntity<?> memberReservationModRoomByEmployee(@PathVariable Long id, @RequestBody EmployeeModRoomDto dto) {

        try {
            employeeRoomService.updateRoomReservation(id, dto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "객실 예약 내역 수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

}
