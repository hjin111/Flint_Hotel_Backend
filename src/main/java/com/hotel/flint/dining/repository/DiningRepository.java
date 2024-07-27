package com.hotel.flint.dining.repository;

import com.hotel.flint.dining.domain.Dining;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningRepository extends JpaRepository<Dining, Long> {
}
