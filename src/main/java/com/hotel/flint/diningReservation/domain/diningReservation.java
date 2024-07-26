package com.hotel.flint.diningReservation.domain;

import com.hotel.flint.diningReservation.dto.ReservationResDto;
import com.hotel.flint.diningReservation.dto.ReservationUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class diningReservation {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    // user 테이블이랑 join 해야 함
    private Long userId;

    // @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 하기
    // @JoinColumn(name = "dining_id")
    private Long diningId;

    @Column(nullable = false)
    private int adult;
    @Column(nullable = false)
    private int child;
    @Column(nullable = false)
    private String comment;

    private LocalDateTime reservationDateTime;

    public ReservationResDto fromEntity(){
        ReservationResDto reservationResDto = ReservationResDto.builder()
                .id(this.id)
                .userId(this.userId)
                .diningId(this.diningId)
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();

        return reservationResDto;
    }

    public void update(ReservationUpdateDto dto){
        this.diningId = dto.getDiningId();
        this.adult = dto.getAdult();
        this.child = dto.getChild();
        this.comment = dto.getComment();
        this.reservationDateTime = dto.getReservationDateTime();

    }

}
