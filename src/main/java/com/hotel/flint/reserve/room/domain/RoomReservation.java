package com.hotel.flint.reserve.room.domain;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.InfoRoomDetResDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private int adultCnt;

    @ColumnDefault("0")
    private int childCnt;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Option parkingYN;

    @Column(nullable = false)
    private String payment;

    @Column(nullable = false)
    private int adultBfCnt; // 성인 조식 신청인원

    @Column(nullable = false)
    private int childBfCnt; // 아이 조식 신청인원

    private String requestContents;


    // member 테이블과 관계설정
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // RoomDetails 테이블과 관계설정
    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomDetails rooms;


    public InfoRoomResDto toInfoRoomResDto(InfoUserResDto infoUserResDto) {
        InfoRoomDetResDto infoRoomDetResDto = InfoRoomDetResDto.builder()
                .roomType(rooms.getRoomInfo().getRoomTypeName())
                .checkin(this.checkInDate)
                .checkout(this.checkOutDate)
                .adultCnt(this.adultCnt)
                .childCnt(this.childCnt)
                .adultBfCnt(this.adultBfCnt)
                .childBfCnt(this.childBfCnt)
                .build();
        return InfoRoomResDto.builder()
                .id(this.id)
                .firstname(infoUserResDto.getFirstName())
                .lastname(infoUserResDto.getLastName())
                .infoRoomDetResDto(infoRoomDetResDto)
                .build();
    }
}