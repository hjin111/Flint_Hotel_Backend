package com.hotel.flint.diningReservation.service;

import com.hotel.flint.diningReservation.domain.diningReservation;
import com.hotel.flint.diningReservation.dto.ReservationReqDto;
import com.hotel.flint.diningReservation.dto.ReservationResDto;
import com.hotel.flint.diningReservation.dto.ReservationUpdateDto;
import com.hotel.flint.diningReservation.repository.diningReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class diningReservationService {

    private final diningReservationRepository diningReservationRepository;

    @Autowired
    public diningReservationService(diningReservationRepository diningReservationRepository){
        this.diningReservationRepository = diningReservationRepository;
    }

    public void create(ReservationReqDto dto){
        diningReservation diningReservation = dto.toEntity();
        diningReservationRepository.save(diningReservation);
    }

    public List<ReservationResDto> list(){

        List<ReservationResDto> reservationResDtos = new ArrayList<>();
        List<diningReservation> diningReservationList = diningReservationRepository.findAll();

        for( diningReservation reservation : diningReservationList ){
            reservationResDtos.add( reservation.fromEntity() );
        }

        return reservationResDtos;
    }

    public void delete(Long id){
        diningReservation diningReservation = diningReservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약 내역이 없습니다"));
        diningReservationRepository.delete(diningReservation);
    }

    @Transactional
    public void update(Long id, ReservationUpdateDto dto){
        diningReservation diningReservation = diningReservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약 내역이 없습니다."));
        diningReservation.update(dto);
    }

}
