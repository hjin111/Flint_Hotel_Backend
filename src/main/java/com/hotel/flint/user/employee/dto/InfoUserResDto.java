package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoUserResDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String nation;
    private LocalDate birthday;
}
