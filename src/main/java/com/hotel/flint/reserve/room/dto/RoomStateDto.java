package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.common.RoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomStateDto {
    private RoomState roomState;
}
