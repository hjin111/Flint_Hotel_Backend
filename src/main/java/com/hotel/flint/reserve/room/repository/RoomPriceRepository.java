package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrices, Long> {
}
