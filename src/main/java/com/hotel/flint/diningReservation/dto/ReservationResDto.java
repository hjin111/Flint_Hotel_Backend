package com.hotel.flint.diningReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResDto {

    private Long id;
    private Long userId;
    private Long diningId;
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;


}