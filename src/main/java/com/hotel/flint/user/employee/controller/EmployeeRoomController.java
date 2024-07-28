package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
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
                                          @RequestBody Map<String, Double> request){
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격 수정 완료", null);
            employeeRoomService.modRoomPrice(room_type_id, request.get("newPrice"));
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }
}
