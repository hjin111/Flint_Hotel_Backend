package com.hotel.flint.employee.domain;


import com.hotel.flint.common.DepartMent;
import com.hotel.flint.common.EmployeeRank;
import com.hotel.flint.common.Gender;
import com.hotel.flint.common.Option;
import com.hotel.flint.employee.dto.EmployeeDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeNumber;

    @Enumerated(EnumType.STRING)
    private EmployeeRank employeeRank;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private LocalDate dateOfEmployment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private Option delYn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartMent department;

    public EmployeeDetResDto EmpDetEntity(){
        return EmployeeDetResDto.builder()
                .id(this.id)
                .employeeNumber(this.employeeNumber)
                .employeeRank(this.employeeRank)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .birthday(this.birthday)
                .dateOfEmployment(this.dateOfEmployment)
                .gender(this.gender)
                .department(this.department)
                .build();
    }
}
