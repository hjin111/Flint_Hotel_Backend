package com.hotel.flint.reserve.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PossibleRoomDto {

    private String roomTypeName;
    private double roomPrice;
}
