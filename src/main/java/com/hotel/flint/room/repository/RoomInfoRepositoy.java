package com.hotel.flint.room.repository;

import com.hotel.flint.room.domain.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomInfoRepositoy extends JpaRepository<RoomInfo, Long> {
}
