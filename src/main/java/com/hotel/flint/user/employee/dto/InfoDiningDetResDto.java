package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.DiningName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfoDiningDetResDto {
    private DiningName diningName;
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;
}
