package com.hotel.flint.room.controller;

import com.hotel.flint.common.RoomState;
import com.hotel.flint.room.domain.RoomDetails;
import com.hotel.flint.room.dto.RoomStateDto;
import com.hotel.flint.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
