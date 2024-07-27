package com.hotel.flint.dining.repository;


import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.dining.domain.Dining;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiningRepository extends JpaRepository<Dining, Long> {

    Optional<Dining> findByDiningName(DiningName diningName);

}