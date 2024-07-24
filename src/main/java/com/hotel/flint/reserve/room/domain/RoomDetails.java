package com.hotel.flint.reserve.room.domain;


import com.hotel.flint.common.RoomState;
import com.hotel.flint.common.RoomView;
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
public class RoomDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int roomNumber; // 룸 넘버

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomState roomState; // 룸 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomView roomView;

    @Column(nullable = false)
    private int maxOccupancy;

    @Column(nullable = false)
    private int roomArea;

    // RoomInfo 테이블과 관계 설정
    @ManyToOne
    @JoinColumn(name = "room_info_id", nullable = false)
    private RoomInfo roomInfo;


}
