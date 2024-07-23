package com.hotel.flint.reserve.room.domain;


import com.hotel.flint.common.Option;
import com.hotel.flint.common.RoomView;
import com.hotel.flint.common.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private Option isHoliday;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'row'")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomView roomView;

    @Column(nullable = false)
    private double additionalPercentage;

    // RoomInfo 테이블과 관계 설정
    @ManyToOne
    @JoinColumn(name = "room_info_id", nullable = false)
    private RoomInfo roomInfo;
}
