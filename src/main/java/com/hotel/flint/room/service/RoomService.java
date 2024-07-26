package com.hotel.flint.room.service;

import com.hotel.flint.room.domain.RoomDetails;
import com.hotel.flint.room.dto.RoomStateDto;
import com.hotel.flint.room.repository.RoomDetailsRepository;
import com.hotel.flint.room.repository.RoomPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class RoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailsRepository roomDetailsRepository;

    @Autowired
    public RoomService(RoomPriceRepository roomPriceRepository, RoomDetailsRepository roomDetailsRepository) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailsRepository = roomDetailsRepository;
    }

    public RoomDetails setRoomState(Long id, RoomStateDto roomStateDto) {
        RoomDetails roomDetails = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));

        roomDetails.updateRoomState(roomStateDto);

        return roomDetailsRepository.save(roomDetails);
    }

}
