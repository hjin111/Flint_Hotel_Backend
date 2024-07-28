package com.hotel.flint.user.employee.controller;

import com.hotel.flint.dining.dto.MenuSaveDto;
import com.hotel.flint.user.employee.service.EmployeeDiningService;
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
public class EmployeeDiningController {
    private final EmployeeDiningService employeeDiningService;

    @Autowired
    public EmployeeDiningController(EmployeeDiningService employeeDiningService){
        this.employeeDiningService = employeeDiningService;
    }

    @PostMapping("/addmenu")
    public ResponseEntity<?> addMenu(@RequestBody MenuSaveDto menuSaveDto) {
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "메뉴가 성공적으로 생성되었습니다", menuSaveDto.getMenuName());
            employeeDiningService.addDiningMenu(menuSaveDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e ){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/modmenu/{id}")
    public ResponseEntity<?> modDiningMenu(@PathVariable Long id,
                                        @RequestBody Map<String, Integer> request) {
        int newCost = request.get("cost");
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격이 변경되었습니다", null);
            employeeDiningService.modDiningMenu(id, newCost);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delmenu/{id}")
    public ResponseEntity<?> delDiningMenu(@PathVariable Long id){
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "메뉴가 삭제되었습니다.", null);
            employeeDiningService.delDiningMenu(id);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }
}
