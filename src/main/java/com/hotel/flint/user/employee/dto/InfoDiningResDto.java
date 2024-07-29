package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoDiningResDto {
    private Long id;
    private String firstname;
    private String lastname;

    private InfoDiningDetResDto infoDiningDetResDto;
}
