package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {
}
