package com.hotel.flint.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeQnaDetailDto {

    private String title;
    private String contents;
    private String memberEmail;
    private LocalDateTime writeTime;

    private String answer;
    private String employeeName;
    private LocalDateTime answerTime;


}
