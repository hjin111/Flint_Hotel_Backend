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
// 기존에 있던 phoneNumber 삭제
public class MemberModResDto {
    private Long id;
    private String email;
    private String afterPassword;
}