package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDetailRepository extends JpaRepository<RoomDetails, Long> {
}