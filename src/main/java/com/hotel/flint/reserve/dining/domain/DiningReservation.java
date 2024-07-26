package com.hotel.flint.reserve.dining.domain;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.reserve.dining.dto.ReservationResDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private LocalDateTime reservationDateTime;
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private Option delYn;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 하기
    @JoinColumn(name = "dining_id")
    private Dining diningId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    public ReservationResDto fromEntity(){
        ReservationResDto reservationResDto = ReservationResDto.builder()
                .id(this.id)
                .memberId(this.memberId)
                .diningId(this.diningId)
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();

        return reservationResDto;
    }

    public void update(ReservationUpdateDto dto){
//        this.diningId = dto.getDiningId();
        this.adult = dto.getAdult();
        this.child = dto.getChild();
        this.comment = dto.getComment();
        this.reservationDateTime = dto.getReservationDateTime();

    }
}