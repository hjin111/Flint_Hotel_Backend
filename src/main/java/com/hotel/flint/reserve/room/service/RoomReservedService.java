package com.hotel.flint.reserve.room.service;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepository;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class RoomReservedService {

    private final RoomReservationRepository roomReservationRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final RoomInfoRepository roomInfoRepository;

    private final HolidayService holidayService;
    private final SeasonService seasonService;

    @Autowired
    public RoomReservedService (RoomReservationRepository roomReservationRepository, RoomDetailRepository roomDetailRepository, RoomInfoRepository roomInfoRepository, HolidayService holidayService, SeasonService seasonService) {
        this.roomReservationRepository = roomReservationRepository;
        this.roomDetailRepository = roomDetailRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.holidayService = holidayService;
        this.seasonService = seasonService;
    }

    /**
     * 룸 예약 진행
     */
    @Transactional
    public double roomReservation(RoomReservedDto dto) {

        // 날짜 가져가서 계산
        double totalPrice = calculatePrice(dto);

        // dto로 계산하고 entity로 바꾸고 save

        return totalPrice;

    }

    private double calculatePrice(RoomReservedDto dto) {

        // 해당 방의 base 가격 가져오기
        double roomBasePrice = getBasePrice(dto.getRoomId());
        double total = 0.0;

        // 체크인,아웃 날짜
        LocalDate checkInDate= dto.getCheckInDate();
        LocalDate checkOutDate = dto.getCheckOutDate();

        // 체크인 체크아웃 날짜를 비교해서 holiday, season 결과 가져오기

        // ... 추가예정 ...


        return total;

    }

    private double getBasePrice(Long id) {

        // room_id를 가지고 해당 방의 detail 정보를 반환
        RoomDetails room = roomDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 없습니다."));

        return room.getRoomInfo().getRoomTypePrice();
    }


}
