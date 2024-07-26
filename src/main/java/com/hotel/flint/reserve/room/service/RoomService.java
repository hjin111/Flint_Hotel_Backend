package com.hotel.flint.reserve.room.service;

import com.hotel.flint.reserve.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.dto.RoomStateDto;
import com.hotel.flint.reserve.room.repository.RoomDetailRepository;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class RoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailRepository roomDetailRepository;

    @Autowired
    public RoomService(RoomPriceRepository roomPriceRepository, RoomDetailRepository roomDetailRepository) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailRepository = roomDetailRepository;
    }

    public RoomDetails setRoomState(Long id, RoomStateDto roomStateDto) {
        RoomDetails roomDetails = roomDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));

        roomDetails.updateRoomState(roomStateDto);

        return roomDetailRepository.save(roomDetails);
    }

}