package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomPricesDto {
    private Long id;
    private Long roomTypeId;
    private Season season;
    private Option isHoliday;
    private RoomView roomView;
    private Double additionalPercentage;
    private Option isWeekend;
}