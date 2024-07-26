package com.hotel.flint.reserve.room.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomTypeName; // 룸 타입명

    @Column(nullable = false)
    private double roomTypePrice; // 룸 별 원가 정보

    @Column(nullable = false)
    private Long roomCnt; // 방 남은 개수

    public void updateRoomStock(Long cnt) {
        this.roomCnt = this.roomCnt - cnt;
    }

    public void updateRoomStockAfterCanceled(Long cnt) {
        this.roomCnt = this.roomCnt + cnt;
    }
}
