package com.hotel.flint.reserve.room.service;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomState;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.domain.RoomPrice;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepository;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

        // 예약 성공 후 룸 상태변경 CO -> RS
        roomDetails.updateRoomStateAfterReservation(RoomState.RS);
        log.info("예약 후 룸의 상태: " + roomDetails.getRoomState().toString());

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

        // 조식 금액 추가하기
        int adultBfCnt = dto.getAdultBfCnt();
        int childBfCnt = dto.getChildBfCnt();

        double bf_total = (adultBfCnt * 50000) + (childBfCnt * 35000);
        log.info("조식 총가격 :" + bf_total);

        total += bf_total;
        log.info("조식 + 객실 total :" + total);

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

    /**
     * 객실 예약 취소
     */
    @Transactional
    public void delete(Long roomReservedId) {

        RoomReservation roomReservation = roomReservationRepository.findById(roomReservedId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 예약 내역 없음")
        );
        roomReservationRepository.delete(roomReservation);

        log.info("예약 취소 전, 남은 방의 개수 : " + roomReservation.getRooms().getRoomInfo().getRoomCnt());
        roomReservation.getRooms().getRoomInfo().updateRoomStockAfterCanceled(1L);
        log.info("예약 취소 후, 남은 방의 개수 : " + roomReservation.getRooms().getRoomInfo().getRoomCnt());

        // 예약 취소 후 룸 상태변경 RS -> CO
        roomReservation.getRooms().updateRoomStateAfterReservation(RoomState.CO);
        log.info("예약 취소 후 룸의 상태: " + roomReservation.getRooms().getRoomState().toString());
    }

    /**
     * 객실 예약 내역 조회 - 목록
     */
    public Page<RoomReservedListDto> roomReservedList(Pageable pageable, Long userId) {

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 회원이 없음")
        );
        Page<RoomReservation> reservations = roomReservationRepository.findByMember(pageable, member);

        log.info("Total reservations found: {}", reservations.getTotalElements());

        // no구하기
        AtomicInteger start = new AtomicInteger((int) pageable.getOffset());

        Page<RoomReservedListDto> roomReservedListDtos = reservations.map((a -> a.listFromEntity(start.incrementAndGet())));

        return roomReservedListDtos;
    }

    /**
     *  객실 예약 내역 조회 - 단 건 상세내역
     */
    public RoomReservedDetailDto roomReservedDetail(Long roomReservationId) {

        RoomReservation detail = roomReservationRepository.findById(roomReservationId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 예약 내역이 없음")
        );

        RoomReservedDetailDto roomReservedDetailDto = detail.detailFromEntity();
        return roomReservedDetailDto;
    }
}