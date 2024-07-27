package com.hotel.flint.user.employee.service;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.reserve.room.repository.RoomDetailsRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepositoy;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EmployeeRoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final RoomInfoRepositoy roomInfoRepositoy;

    @Autowired
    public EmployeeRoomService(RoomPriceRepository roomPriceRepository, RoomDetailsRepository roomDetailsRepository, RoomInfoRepositoy roomInfoRepositoy) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomInfoRepositoy = roomInfoRepositoy;
    }

    public void setRoomState(Long id, RoomStateDto roomStateDto) {
        RoomDetails roomDetails = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 객실은 존재하지 않습니다."));

        roomDetails.updateRoomState(roomStateDto);
    }

    public void modRoomPrice(Long id, Double newPrice){
        RoomInfo roomInfo = roomInfoRepositoy.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("해당 타입의 객실은 존재하지 않습니다."));

        roomInfo.updateRoomPrice(newPrice);

    }

}
