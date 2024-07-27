package com.hotel.flint.user.member.domain;

import com.hotel.flint.common.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName; // 이름

    @Column(nullable = false)
    private String lastName; // 성

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'N'")
    private Option delYN;

//    public Member updatePassword(MemberUpdateDto memberUpdateDto){
//
//    }

}
