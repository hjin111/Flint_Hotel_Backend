package com.hotel.flint.user.member.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberModResDto {
    private Long id;
    private String password;
    private String phoneNumber;
}