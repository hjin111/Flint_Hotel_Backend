package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.reserve.room.domain.ReservedRoom;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckedReservedDateDto {

    private Long room_id; // 방번호
    private LocalDate date;
}