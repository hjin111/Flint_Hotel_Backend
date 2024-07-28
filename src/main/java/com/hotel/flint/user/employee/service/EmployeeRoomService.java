package com.hotel.flint.user.employee.service;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EmployeeRoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final EmployeeService employeeService;
    private final RoomReservationRepository roomReservationRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public EmployeeRoomService(RoomPriceRepository roomPriceRepository,
                               RoomDetailRepository roomDetailRepository,
                               EmployeeService employeeService,
                               RoomReservationRepository roomReservationRepository,
                               MemberRepository memberRepository) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailRepository = roomDetailRepository;
        this.employeeService = employeeService;
        this.roomReservationRepository = roomReservationRepository;
        this.memberRepository = memberRepository;
    }

    public RoomDetails setRoomState(Long id, RoomStateDto roomStateDto) {
        RoomDetails roomDetails = roomDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));

        roomDetails.updateRoomState(roomStateDto);

        return roomDetailRepository.save(roomDetails);
    }

    public InfoRoomResDto memberReservationRoomCheck(Long id){

        Member member = memberRepository.findById(id).orElse(null);

        RoomReservation roomReservation = roomReservationRepository.findByMember(member);
        InfoUserResDto infoUserResDto = employeeService.memberInfo(id);

        InfoRoomResDto infoRoomResDto = roomReservation.toInfoRoomResDto(infoUserResDto);

        return infoRoomResDto;
    }

//    고객 객실 예약 취소하는 메서드
    public void memberReservationCncRoomByEmployee(InfoRoomResDto dto){
        roomReservationRepository.deleteById(dto.getId());
    }
}