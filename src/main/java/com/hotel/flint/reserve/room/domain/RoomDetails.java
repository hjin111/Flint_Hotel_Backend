package com.hotel.flint.reserve.room.domain;

import com.hotel.flint.common.enumdir.RoomState;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomInfo roomInfo;

    @Column(nullable = false, unique = true)
    private Integer roomNumber;

    @Enumerated(value = EnumType.STRING)
    private RoomState roomState;

    @Enumerated(value = EnumType.STRING)
    private RoomView roomView;

    @Column(nullable = false)
    private Integer maxOccupancy;
    @Column(nullable = false)
    private Integer roomArea;

    /**
     * 직원이 룸의 상태를 변경
     */
    public void updateRoomState(RoomStateDto roomStateDto){
        this.roomState = roomStateDto.getRoomState();
    }

    /**
     * 고객이 룸 예약 진행 혹은 취소 후 상태 자동 변경
     */
    public void updateRoomStateAfterReservation(RoomState roomState) {
        this.roomState = roomState;
    }

}