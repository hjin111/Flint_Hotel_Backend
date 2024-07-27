package com.hotel.flint.room.domain;

import com.hotel.flint.common.RoomState;
import com.hotel.flint.common.RoomView;
import com.hotel.flint.room.dto.RoomStateDto;
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

    private Integer roomNumber;

    @Enumerated(value = EnumType.STRING)
    private RoomState roomState;

    @Enumerated(value = EnumType.STRING)
    private RoomView view;

    private Integer maxOccupancy;
    private Integer area;

    public void updateRoomState(RoomStateDto roomStateDto){
        this.roomState = roomStateDto.getRoomState();
    }
}
