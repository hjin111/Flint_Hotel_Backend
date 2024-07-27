package com.hotel.flint.employee.repository;

import com.hotel.flint.employee.domain.Dining;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningRepository extends JpaRepository<Dining, Long> {
}
