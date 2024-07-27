package com.hotel.flint.employee.controller;

import com.hotel.flint.employee.dto.MenuSaveDto;
import com.hotel.flint.employee.service.DiningService;
import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
    public ResponseEntity<?> addMenu(@RequestBody MenuSaveDto menuSaveDto) {
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "메뉴가 성공적으로 생성되었습니다", menuSaveDto.getMenuName());
            diningService.addDiningMenu(menuSaveDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/modmenu/{id}")
    public ResponseEntity<?> modDiningMenu(@PathVariable Long id,
                                        @RequestBody Map<String, Integer> request) {
        int newCost = request.get("cost");
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격이 변경되었습니다", null);
            diningService.modDiningMenu(id, newCost);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delmenu/{id}")
    public ResponseEntity<?> delDiningMenu(@PathVariable Long id){
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "메뉴가 삭제되었습니다.", null);
            diningService.delDiningMenu(id);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

}
