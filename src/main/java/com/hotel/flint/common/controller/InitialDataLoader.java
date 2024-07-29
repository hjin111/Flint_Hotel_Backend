package com.hotel.flint.common.controller;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.EmployeeRank;
import com.hotel.flint.common.enumdir.Gender;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.EmployeeMakeDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    실행 시점에 office 부서의 유저 한개를 만들겠다.
    @Override
    public void run(String... args) throws Exception{
        if (employeeRepository.findByEmailAndDelYN("flinthotelcom@gmail.com", Option.N).isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate date = LocalDate.parse("2024/07/29", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                            .firstName("flint")
                            .lastName("hotel")
                            .email("flinthotelcom@gmail.com")
                            .phoneNumber("02-1111-2222")
                            .departMent(Department.Office)
                            .password("12341234")
                    .employeeRank(EmployeeRank.사장)
                            .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.FEMALE)
                            .employeeNumber("FL0000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
    }
}
