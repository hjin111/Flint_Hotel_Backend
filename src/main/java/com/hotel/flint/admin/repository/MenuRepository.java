package com.hotel.flint.admin.repository;

import com.hotel.flint.admin.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuName(String name);
}
