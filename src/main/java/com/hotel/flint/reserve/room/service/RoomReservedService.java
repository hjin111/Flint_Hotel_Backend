package com.hotel.flint.reserve.room.service;

import com.hotel.flint.common.Option;
import com.hotel.flint.common.RoomView;
import com.hotel.flint.common.Season;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepository;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.user.member.domain.User;
import com.hotel.flint.user.member.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RoomReservedService {

    private final RoomReservationRepository roomReservationRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final UserRepository userRepository;

    private final HolidayService holidayService;
    private final SeasonService seasonService;

    @Autowired
    public RoomReservedService (RoomReservationRepository roomReservationRepository, RoomDetailRepository roomDetailRepository, RoomPriceRepository roomPriceRepository, RoomInfoRepository roomInfoRepository, UserRepository userRepository, HolidayService holidayService, SeasonService seasonService) {
        this.roomReservationRepository = roomReservationRepository;
        this.roomDetailRepository = roomDetailRepository;
        this.roomPriceRepository = roomPriceRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.userRepository = userRepository;

        this.holidayService = holidayService;
        this.seasonService = seasonService;
    }

    /**
     * 룸 예약 진행
     */
    @Transactional
    public double roomReservation(RoomReservedDto dto, Long userId) {

        // 날짜 가져가서 계산
        double totalPrice = calculatePrice(dto);

        // dto를 entity로 바꾸고 save
        // user 찾기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 회원이 없음")
        );
        // RoomDetail 찾아오기
        RoomDetails roomDetails = roomDetailRepository.findById(dto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 방이 없음")
        );

        RoomReservation roomReservation = dto.toEntity(user, roomDetails);
        RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);
        log.info("room reservation : " + savedRoomReservation);

        return totalPrice;

    }

    /**
     * 예약 룸 총액 계산
     */
    private double calculatePrice(RoomReservedDto dto) {

        // 해당 방의 base 가격 가져오기
        double roomBasePrice = getBasePrice(dto.getRoomId());
        double total = 0.0;

        // 체크인,아웃 날짜
        LocalDate checkInDate= dto.getCheckInDate();
        LocalDate checkOutDate = dto.getCheckOutDate();

        // 체크인 체크아웃 날짜를 비교해서 holiday, season 결과 가져오기
        // 뷰 찾기
        Long roomId = dto.getRoomId();
        RoomDetails roomDetails = roomDetailRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 없습니다."));
        while (checkInDate.isBefore(checkOutDate)) { // checkInDate <= checkOutdate
            boolean isHoliday = holidayService.isHoliday(checkInDate);
            boolean isSeason = seasonService.isSeason(checkInDate);
            log.info("isHoliday:" + checkInDate + isHoliday);

            double percentage = getAdditionalPercentage(roomDetails.getId(), roomDetails.getRoomInfo(), isHoliday, isSeason, String.valueOf(roomDetails.getRoomView()));

            // 총액 계산
            log.info("roomBasePrice " + roomBasePrice);
            log.info("percentage " + percentage);
            total += roomBasePrice * percentage;
            log.info("total :" + total);
            checkInDate = checkInDate.plusDays(1); // 체크인날짜 +1 (체크아웃 전까지)
        }
        return total;
    }

    /**
     * 룸의 원가 가져오기
     */
    private double getBasePrice(Long id) {

        // room_id를 가지고 해당 방의 detail 정보를 반환
        RoomDetails room = roomDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 없습니다."));

        return room.getRoomInfo().getRoomTypePrice();
    }

    /**
     * 각 방의 정보 확인해서 퍼센티지 가져오기
     */
    private double getAdditionalPercentage(Long roomId, RoomInfo roomInfo, boolean isHoliday, boolean isSeason, String roomView) {
        log.info("isHoliday: " + isHoliday);
        Option holiday = isHoliday ? Option.Y : Option.N;
        Season season = isSeason ? Season.PEAK : Season.ROW;
        RoomDetails findRoom = roomDetailRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("해당 id의 방이 없습니다."));
        RoomView view = findRoom.getRoomView();
        log.info("holiday : " + holiday);
        log.info("season : " + season);
        log.info("view : " + view);

        RoomPrice percentage = roomPriceRepository.findByIsHolidayAndSeasonAndRoomViewAndRoomInfo(holiday, season, view, roomInfo);

        return percentage != null ? percentage.getAdditionalPercentage() : 1.0;

    }


}
