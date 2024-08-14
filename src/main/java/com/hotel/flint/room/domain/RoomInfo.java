package com.hotel.flint.room.domain;

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
    private long roomTypePrice;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPrice> roomPrices;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomDetails> roomDetails;

    public void updateRoomPrice(long newPrice){
        this.roomTypePrice = newPrice;
    }
}
