package com.hotel.flint.reserve.dining.domain;

import com.hotel.flint.common.domain.BaseTimeEntity;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.reserve.dining.dto.ReservationDetailDto;
import com.hotel.flint.reserve.dining.dto.ReservationListResDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
import com.hotel.flint.user.employee.dto.InfoDiningDetResDto;
import com.hotel.flint.user.employee.dto.InfoDiningResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.member.domain.Member;
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
public class DiningReservation extends BaseTimeEntity {

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

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 하기
    @JoinColumn(name = "dining_id")
    private Dining diningId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    // 예약 전체 조회
    public ReservationListResDto fromEntity(){
        ReservationListResDto reservationListResDto = ReservationListResDto.builder()
                .id(this.id)
                .memberId(this.memberId.getId())
                .diningName(this.diningId.getDiningName())
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();

        return reservationListResDto;
    }

    // 예약 삭제시 필요
    public DiningReservation(Member member, Long id){
        this.memberId = member;
        this.id = id;
    }

    // 예약 수정시 필요
    public DiningReservation( Long id, Member member, Dining dining, ReservationUpdateDto dto){

        this.id = id;
        this.adult = dto.getAdult();
        this.child = dto.getChild();
        this.comment = dto.getComment();
        this.reservationDateTime = dto.getReservationDateTime();
        this.diningId = dining;
        this.memberId = member;

    }

    // 예약 단건 조회
    public ReservationDetailDto fromEntity(Long diningReservationId, Member member){

        return ReservationDetailDto.builder()
                .id(diningReservationId)
                .memberId(member.getId())
                .diningName(this.diningId.getDiningName())
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public InfoDiningResDto toInfoDiningResDto(InfoUserResDto infoUserResDto){
        InfoDiningDetResDto infoDiningDetResDto = InfoDiningDetResDto.builder()
                .diningName(this.diningId.getDiningName())
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();

        return InfoDiningResDto.builder()
                .id(infoUserResDto.getId())
                .firstname(infoUserResDto.getFirstName())
                .lastname(infoUserResDto.getLastName())
                .infoDiningDetResDto(infoDiningDetResDto)
                .build();
    }
}