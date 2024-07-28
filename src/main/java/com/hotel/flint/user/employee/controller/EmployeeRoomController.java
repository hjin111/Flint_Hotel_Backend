package com.hotel.flint.user.employee.controller;

import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.service.EmployeeRoomService;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee")
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

    
//    고객의 예약 정보를 가져오는 로직
    @PostMapping("/reserve")
    @ResponseBody
    public InfoRoomResDto memberReservationRoomCheck(@RequestParam("id") Long id){
        InfoRoomResDto infoRoomResDto = employeeRoomService.memberReservationRoomCheck(id);

        return infoRoomResDto;
    }

//    고객의 객실 예약 취소하는 로직
    @PostMapping("/cancel_reserve_dining/{id}")
    @ResponseBody
    public String memberReservationCncDiningByEmployee(@RequestBody Long id){
        InfoRoomResDto infoRoomResDto = employeeRoomService.memberReservationRoomCheck(id);
        employeeRoomService.memberReservationCncRoomByEmployee(infoRoomResDto);
        return "삭제 완료";
    }
}
