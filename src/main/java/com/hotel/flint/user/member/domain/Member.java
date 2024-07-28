package com.hotel.flint.user.member.domain;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.member.dto.MemberDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String nation;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Option delYN;


    public InfoUserResDto infoUserEntity(){
        return InfoUserResDto.builder()
                .id(this.id)
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .nation(this.nation)
                .birthday(this.birthday)
                .build();
    }

    public MemberDetResDto detUserEntity(){
        return MemberDetResDto.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .nation(this.nation)
                .password(this.password)
                .birthday(this.birthday)
                .build();
    }

    public Member deleteUser(){
        this.delYN = Option.Y;
        return this;
    }

    public void modifyUser(String password){
        this.password = password;
    }
}