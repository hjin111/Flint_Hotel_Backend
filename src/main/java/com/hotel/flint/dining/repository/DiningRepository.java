package com.hotel.flint.dining.repository;


import com.hotel.flint.dining.domain.Dining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningRepository extends JpaRepository<Dining, Long> {
}