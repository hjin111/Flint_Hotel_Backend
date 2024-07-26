package com.hotel.flint.dining.domain;

import com.hotel.flint.common.DiningName;
import com.hotel.flint.common.Option;
import com.hotel.flint.diningReservation.domain.diningReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Dining {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    @Enumerated(EnumType.STRING)
    private DiningName diningName;

    @Column( length = 255, nullable = false)
    private String location;

    @Column( nullable = false )
    @Enumerated(EnumType.STRING)
    private Option breakfastYn;

    @Column( nullable = false )
    private String callNum;

    @Column( nullable = false )
    private LocalTime openTime;

    @Column( nullable = false )
    private LocalTime closeTime;

}
