package com.hotel.flint.employee.dto;

import com.hotel.flint.employee.domain.Dining;
import com.hotel.flint.employee.domain.Menu;
import lombok.Data;

@Data
public class MenuSaveDto {
    private String menuName;
    private int cost;
    private Long diningId;

    public Menu toEntity(Dining dining){
        Menu menu = Menu.builder()
                .menuName(this.menuName)
                .cost(this.cost)
                .dining(dining) // dining 객체 설정
                .build();
        return menu;
    }
}
