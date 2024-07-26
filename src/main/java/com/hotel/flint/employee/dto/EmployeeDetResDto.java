package com.hotel.flint.employee.dto;

import com.hotel.flint.common.enumdir.DepartMent;
import com.hotel.flint.common.enumdir.EmployeeRank;
import com.hotel.flint.common.enumdir.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDetResDto {
    private Long id;
    private String employeeNumber;
    private EmployeeRank employeeRank;
    private String email;
    private String phoneNumber;
    private LocalDate birthday;
    private LocalDate dateOfEmployment;
    private Gender gender;
    private DepartMent department;
}
