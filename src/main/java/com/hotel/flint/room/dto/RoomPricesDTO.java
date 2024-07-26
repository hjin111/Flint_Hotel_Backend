package com.hotel.flint.room.dto;

import com.hotel.flint.common.Option;
import com.hotel.flint.common.RoomView;
import com.hotel.flint.common.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomPricesDTO {
    private Long id;
    private Long roomTypeId;
    private Season season;
    private Option isHoliday;
    private RoomView roomView;
    private Double additionalPercentage;
    private Option isWeekend;
}

