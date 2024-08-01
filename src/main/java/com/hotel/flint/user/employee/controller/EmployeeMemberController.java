package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.user.employee.dto.*;
import com.hotel.flint.user.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/employee")
public class EmployeeMemberController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeMemberController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

//    직원이 멤버에 대한 정보를 조회하는 기능.
    @PostMapping("/userinfo")
    @ResponseBody
    public InfoUserResDto memberInfoCheck(@RequestParam String email){
        return employeeService.memberInfo(email);
    }

//    직원 계정의 정보 조회
//    해당 직원 id값
    @GetMapping("/detail")
    @ResponseBody
    public ResponseEntity<?> empDetail(){
        try{
            EmployeeDetResDto dto = employeeService.employeeDetail();
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Details Find" , dto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

//    직원 정보 수정 : 비밀번호 수정만 있음.
//    해당 직원 id, 수정할 값이 들어있음.
    @PutMapping("/modify")
    @ResponseBody
    public ResponseEntity<?> userModify(@RequestBody EmployeeModResDto dto){
        try {
            employeeService.employeeModify(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Password Modify" , null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

//    직원의 직급을 수정
//    직원 수정은 office 부서만 가능
//    dto 안에 수정하는 직원 id, 수정 대상 id, 수정할 값 이 들어있다.
    @PutMapping("/mod_rank")
    @ResponseBody
    public ResponseEntity<?> modEmployeeRank(@RequestBody EmployeeRankModResDto dto){
        try {
            employeeService.modEmployeeRank(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Rank Modify" , null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }
        catch(EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list_reserve")
    @ResponseBody
    public ResponseEntity<?> employeeMemberReserveList(@RequestParam String email){
        try {
            InfoMemberReseveListResDto info = employeeService.employeeMemberReserveList(email);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Member Reserve List" , info);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }
        catch(EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}