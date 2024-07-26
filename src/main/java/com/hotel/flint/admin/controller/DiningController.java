package com.hotel.flint.admin.controller;

import com.hotel.flint.admin.dto.MenuSaveDto;
import com.hotel.flint.admin.service.DiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
