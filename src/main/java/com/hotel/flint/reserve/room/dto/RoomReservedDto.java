package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.common.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservedDto {

    private int adultCnt; // 투숙하는 성인 수
    private int childCnt; // 투숙하는 아이 수
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private int adultBfCnt; // 성인 조식 신청 인원
    private int childBfCnt; // 아이 조식 신청 인원
    private Option parkingYN; // 주차 여부
    private String requestContents; // 요청사항

    private Long roomId; // 일단 사용자가 룸을 직접 지정한다고 가정 후 추후에 수정
}
