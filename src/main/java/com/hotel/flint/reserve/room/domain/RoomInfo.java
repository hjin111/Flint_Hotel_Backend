package com.hotel.flint.reserve.room.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomTypeName;
    private Double roomTypePrice;

//    방 남은 개수
    @Column(nullable = false)
    private Long roomCnt;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPrice> roomPrices;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomDetails> roomDetails;

    /**
     * 고객이 룸 예약 진행 후 룸 개수 자동 -1
     */
    public void updateRoomStock(Long cnt) {
        this.roomCnt = this.roomCnt - cnt;
    }

    /**
     * 고객이 룸 예약 취소 후 룸 개수 자동 +1
     */
    public void updateRoomStockAfterCanceled(Long cnt) {
        this.roomCnt = this.roomCnt + cnt;
    }
}