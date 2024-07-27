package com.hotel.flint.employee.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private String menuName;

    @JoinColumn(name = "dining_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dining dining;


    public void menuUpdate(int newCost){
        this.cost = newCost;
    }
}
