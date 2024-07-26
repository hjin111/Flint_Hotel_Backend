package com.hotel.flint.reserve.room.service;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepository;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RoomReservedService {

    private final RoomReservationRepository roomReservationRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final MemberRepository memberRepository;

    private final HolidayService holidayService;
    private final SeasonService seasonService;

    @Autowired
    public RoomReservedService (RoomReservationRepository roomReservationRepository, RoomDetailRepository roomDetailRepository, RoomPriceRepository roomPriceRepository, RoomInfoRepository roomInfoRepository, MemberRepository memberRepository, HolidayService holidayService, SeasonService seasonService) {
        this.roomReservationRepository = roomReservationRepository;
        this.roomDetailRepository = roomDetailRepository;
        this.roomPriceRepository = roomPriceRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.memberRepository = memberRepository;

        this.holidayService = holidayService;
        this.seasonService = seasonService;
    }

    /**
     * 룸 예약 진행
     */
    @Transactional
    public double roomReservation(RoomReservedDto dto, Long userId) {

        // user 찾기
        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 회원이 없음")
        );
        // RoomDetail 찾아오기
        RoomDetails roomDetails = roomDetailRepository.findById(dto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 방이 없음")
        );

        // CO(check out) 상태의 룸만 예약 가능
        if (!roomDetails.getRoomState().toString().equals("CO")) {
            throw new IllegalArgumentException("예약 가능한 룸의 상태가 아님");
        }

        // 수용할 수 있는 최대 인원수 체크하기
        if (roomDetails.getMaxOccupancy() < dto.getAdultCnt() + dto.getChildCnt()) {
            throw new IllegalArgumentException("최대 수용 가능한 인원 수 초과");
        }

        RoomReservation roomReservation = dto.toEntity(member, roomDetails);
        RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);
        log.info("room reservation : " + savedRoomReservation);

        // 예약 save가 되면 -> roomInfo 테이블의 roomCnt를 -1로 업데이트
        log.info("예약 전, 남은 방의 개수 : " + roomDetails.getRoomInfo().getRoomCnt());
        roomDetails.getRoomInfo().updateRoomStock(1L);
        log.info("예약 후, 남은 방의 개수 : " + roomDetails.getRoomInfo().getRoomCnt());

        // 날짜 가져가서 계산
        double totalPrice = calculatePrice(dto);

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
            log.info("Checking date: {} isHoliday: {}, isSeason: {}", checkInDate, isHoliday, isSeason);

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