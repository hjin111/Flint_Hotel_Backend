package com.hotel.flint.user.domain;

import com.hotel.flint.common.Option;
import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.user.dto.UserDetResDto;
import com.hotel.flint.user.dto.UserModResDto;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
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
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private Option delYn;

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

    public UserDetResDto detUserEntity(){
        return UserDetResDto.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .nation(this.nation)
                .password(this.password)
                .birthday(this.birthday)
                .build();
    }

    public User deleteUser(){
        this.delYn = Option.Y;
        return this;
    }

    public void modifyUser(String password){
        this.password = password;
    }
}
