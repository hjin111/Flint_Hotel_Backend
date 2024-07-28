package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import com.hotel.flint.reserve.room.domain.RoomPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrices, Long> {
    RoomPrice findByIsHolidayAndSeasonAndRoomViewAndRoomInfo(Option isHoliday, Season season, RoomView roomView, RoomInfo roomInfo);
}
