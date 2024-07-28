package com.hotel.flint.user.employee.dto;


import com.hotel.flint.common.enumdir.EmployeeRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRankModResDto {
    private Long officeId;
    private Long targetId;
    private EmployeeRank employeeRank;
}