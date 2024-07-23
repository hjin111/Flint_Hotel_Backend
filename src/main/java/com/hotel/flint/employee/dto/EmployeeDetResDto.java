package com.hotel.flint.employee.dto;

import com.hotel.flint.common.DepartMent;
import com.hotel.flint.common.EmployeeRank;
import com.hotel.flint.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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
