package com.hotel.flint.user.employee.controller;


import com.hotel.flint.dining.dto.MenuSaveDto;
import com.hotel.flint.dining.service.DiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/employee/dining")
public class EmployeeDiningController {
    @Autowired
    private final DiningService diningService;

    @Autowired
    public EmployeeDiningController(DiningService diningService){
        this.diningService = diningService;
    }

    @PostMapping("/addmenu")
    public void addMenu(@RequestBody MenuSaveDto menuSaveDto) {
        diningService.addDiningMenu(menuSaveDto);
    }

    @PatchMapping("/modmenu/{id}")
    public void modDiningMenu(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        int newCost = request.get("cost");
        diningService.modDiningMenu(id, newCost);
    }

    @DeleteMapping("/delmenu/{id}")
    public void delDiningMenu(@PathVariable Long id){
        diningService.delDiningMenu(id);
    }

}
