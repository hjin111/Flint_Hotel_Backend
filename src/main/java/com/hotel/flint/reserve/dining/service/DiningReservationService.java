package com.hotel.flint.reserve.dining.service;

import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.ReservationSaveReqDto;
import com.hotel.flint.reserve.dining.dto.ReservationResDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiningReservationService {

    private final DiningReservationRepository diningReservationRepository;

    @Autowired
    public DiningReservationService(DiningReservationRepository diningReservationRepository){
        this.diningReservationRepository = diningReservationRepository;
    }

    public void create(ReservationSaveReqDto dto){
        DiningReservation diningReservation = dto.toEntity();
        diningReservationRepository.save(diningReservation);
    }

    public List<ReservationResDto> list(){

        List<ReservationResDto> reservationResDtos = new ArrayList<>();
        List<DiningReservation> diningReservationList = diningReservationRepository.findAll();

        for(DiningReservation reservation : diningReservationList ){
            reservationResDtos.add( reservation.fromEntity() );
        }

        return reservationResDtos;
    }

    public void delete(Long id){
        DiningReservation diningReservation = diningReservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약 내역이 없습니다"));
        diningReservationRepository.delete(diningReservation);
    }

    @Transactional
    public void update(Long id, ReservationUpdateDto dto){
        DiningReservation diningReservation = diningReservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약 내역이 없습니다."));
        diningReservation.update(dto);
    }

}