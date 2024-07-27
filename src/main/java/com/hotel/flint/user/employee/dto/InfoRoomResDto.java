package com.hotel.flint.user.employee.dto;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoRoomResDto {

    private Long id;
    private String firstname;
    private String lastname;

    private InfoRoomDetResDto infoRoomDetResDto;

}
