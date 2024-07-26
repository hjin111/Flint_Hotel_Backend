package com.hotel.flint.admin.domain;

import com.hotel.flint.common.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Dining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false, unique = true, length = 13)
    private String callNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiningName diningName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Option breakfastYn;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @OneToMany(mappedBy = "dining", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Menu> menu;


}

enum DiningName {
    KorDining,
    ChiDining,
    JapDining,
    Lounge
}
