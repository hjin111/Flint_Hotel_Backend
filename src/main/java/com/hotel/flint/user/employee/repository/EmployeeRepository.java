package com.hotel.flint.user.employee.repository;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmailAndDelYN(String email, Option delYN);
    Optional<Employee> findByPhoneNumberAndDelYN(String phoneNumber, Option delYN);
}

