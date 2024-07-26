package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomDetailRepository extends JpaRepository<RoomDetails, Long> {

}
