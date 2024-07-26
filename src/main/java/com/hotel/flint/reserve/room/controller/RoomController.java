package com.hotel.flint.reserve.room.controller;

import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.reserve.room.service.RoomService;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PatchMapping("/setstate/{room_id}")
    public ResponseEntity<RoomDetails> setRoomState(@PathVariable Long room_id,
                                                    @RequestBody RoomStateDto roomStateDto) {
        RoomDetails updatedRoomDetails = roomService.setRoomState(room_id, roomStateDto);
        return ResponseEntity.ok(updatedRoomDetails);
    }
}
