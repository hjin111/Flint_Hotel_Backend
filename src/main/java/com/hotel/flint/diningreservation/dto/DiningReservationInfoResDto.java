package com.hotel.flint.diningreservation.dto;

import com.hotel.flint.common.DiningName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiningReservationInfoResDto {

    private DiningName diningName;
    private String firstName;
    private String lastName;
    private int adultCount;
    private int childCount;
    private String comment;
    private LocalDateTime reservationDate;

}
