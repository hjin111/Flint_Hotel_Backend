package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomInfoRepositoy extends JpaRepository<RoomInfo, Long> {
}
