package com.hotel.flint.reserve.room.domain;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
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

    public RoomReservedListDto listFromEntity(int no) {

        RoomReservedListDto roomReservedListDto = RoomReservedListDto.builder()
                .no(no)
                .roomType(this.getRooms().getRoomInfo().getRoomTypeName())
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .build();
        return roomReservedListDto;
    }

    public RoomReservedDetailDto detailFromEntity() {
        RoomReservedDetailDto roomReservation = RoomReservedDetailDto.builder()
                .adultCnt(this.adultCnt)
                .childCnt(this.childCnt)
                .roomType(this.getRooms().getRoomInfo().getRoomTypeName())
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .parkingYN(this.parkingYN)
                .payment("kakaopay")
                .adultBfCnt(this.adultBfCnt)
                .childBfCnt(this.childBfCnt)
                .requestContents(this.requestContents)
                .build();

        return roomReservation;
    }

}