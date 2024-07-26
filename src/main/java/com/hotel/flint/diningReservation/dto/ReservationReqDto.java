package com.hotel.flint.diningReservation.dto;

import com.hotel.flint.diningReservation.domain.diningReservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationReqDto {

    private Long userId;
    private Long diningId;
    private int adult;
    private int child;
    private String comment;

    private LocalDateTime reservationDateTime;

    public diningReservation toEntity(){
        return diningReservation.builder()
                .userId(this.userId)
                .diningId(this.diningId)
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();
    }

}
