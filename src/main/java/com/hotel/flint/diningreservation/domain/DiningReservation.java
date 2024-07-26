package com.hotel.flint.diningreservation.domain;

import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.diningreservation.dto.DiningReservationInfoResDto;
import com.hotel.flint.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiningReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int adult;
    @Column(nullable = false)
    private int child;
    @Column
    private String comment;
    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 하기
    @JoinColumn(name = "dining_id")
    private Dining diningId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;


    public DiningReservationInfoResDto toEntity(){
        return DiningReservationInfoResDto.builder()
                .diningName(this.diningId.getDiningName())
                .firstName(this.memberId.getFirstName())
                .lastName(this.memberId.getLastName())
                .adultCount(this.adult)
                .childCount(this.child)
                .comment(this.comment)
                .reservationDate(this.reservationDate)
                .build();
    }
}
