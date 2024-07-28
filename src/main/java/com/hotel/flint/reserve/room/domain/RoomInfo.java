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

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPrices> roomPrices;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomDetails> roomDetails;

    public void updateRoomPrice(Double newPrice){
        this.roomTypePrice = newPrice;
    }
}
