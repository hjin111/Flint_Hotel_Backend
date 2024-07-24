package com.hotel.flint.reserve.room.domain;

import com.hotel.flint.common.Option;
import com.hotel.flint.user.member.domain.User;
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
    @ColumnDefault("'N'")
    private Option parkingYN;

    @Column(nullable = false)
    private String payment;

    @Column(nullable = false)
    private int adultBfCnt; // 성인 조식 신청인원

    @Column(nullable = false)
    private int childBfCnt; // 아이 조식 신청인원

    private String requestContents;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private Option delYN;


    // user 테이블과 관계설정
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // RoomDetails 테이블과 관계설정
    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomDetails roomDetails;

}
