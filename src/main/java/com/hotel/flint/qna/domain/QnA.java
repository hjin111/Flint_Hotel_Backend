package com.hotel.flint.qna.domain;

import com.hotel.flint.common.domain.BaseTimeEntity;
import com.hotel.flint.common.enumdir.DepartMent;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QnA extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated
    @Column(nullable = false)
    private DepartMent service;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String comment;

    private Option respond;

    @Column
    private String answer;


}
