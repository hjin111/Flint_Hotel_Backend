package com.hotel.flint.member.domain;

import com.hotel.flint.common.Option;
import com.hotel.flint.diningreservation.domain.DiningReservation;
import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.member.dto.MemberDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
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

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    private List<DiningReservation> diningReservationList;

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
        this.delYn = Option.Y;
        return this;
    }

    public void modifyUser(String password){
        this.password = password;
    }
}
