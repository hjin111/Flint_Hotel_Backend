package com.hotel.flint.user.employee.controller;

import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.user.employee.service.EmployeeRoomService;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EmployeeRoomController {
    private final EmployeeRoomService employeeRoomService;

    @Autowired
    public EmployeeRoomController(EmployeeRoomService employeeRoomService) {
        this.employeeRoomService = employeeRoomService;
    }

    @PatchMapping("/setstate/{room_id}")
    public ResponseEntity<RoomDetails> setRoomState(@PathVariable Long room_id,
                                                    @RequestBody RoomStateDto roomStateDto) {
        RoomDetails updatedRoomDetails = employeeRoomService.setRoomState(room_id, roomStateDto);
        return ResponseEntity.ok(updatedRoomDetails);
    }
}
