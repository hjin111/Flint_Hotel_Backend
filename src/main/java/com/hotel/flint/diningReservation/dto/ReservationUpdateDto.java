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
public class ReservationUpdateDto {

    private Long diningId; // 한식, 중식, 일식, 라운지 구분하는 ID
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;

}
