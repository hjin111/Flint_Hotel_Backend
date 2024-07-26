package com.hotel.flint.diningReservation.repository;

import com.hotel.flint.diningReservation.domain.diningReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface diningReservationRepository extends JpaRepository <diningReservation, Long> {
}
