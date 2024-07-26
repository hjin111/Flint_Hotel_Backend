package com.hotel.flint.admin.controller;

import com.hotel.flint.admin.dto.MenuSaveDto;
import com.hotel.flint.admin.service.DiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/dining")
public class DiningController {
    private final DiningService diningService;

    @Autowired
    public DiningController(DiningService diningService){
        this.diningService = diningService;
    }

    @PostMapping("/addmenu")
    public void addMenu(@RequestBody MenuSaveDto menuSaveDto) {
        diningService.addDiningMenu(menuSaveDto);
    }

}
