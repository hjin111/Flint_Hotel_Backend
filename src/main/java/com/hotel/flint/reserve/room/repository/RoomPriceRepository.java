package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.common.Option;
import com.hotel.flint.common.RoomView;
import com.hotel.flint.common.Season;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {

    RoomPrice findByIsHolidayAndSeasonAndRoomViewAndRoomInfo(Option isHoliday, Season season, RoomView roomView, RoomInfo roomInfo);
}
