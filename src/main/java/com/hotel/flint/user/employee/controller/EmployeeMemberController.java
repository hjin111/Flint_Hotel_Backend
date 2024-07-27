package com.hotel.flint.user.employee.controller;

import com.hotel.flint.user.employee.dto.EmployeeDetResDto;
import com.hotel.flint.user.employee.dto.EmployeeModResDto;
import com.hotel.flint.user.employee.dto.EmployeeRankModResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/employee")
public class EmployeeMemberController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeMemberController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

//    직원이 멤버에 대한 정보를 조회하는 기능.
    @PostMapping("/userinfo/{id}")
    @ResponseBody
    public InfoUserResDto memberInfoCheck(@PathVariable Long id){
        return employeeService.memberInfo(id);
    }

//    직원 계정의 정보 조회
//    해당 직원 id값
    @PostMapping("/detail/{id}")
    @ResponseBody
    public EmployeeDetResDto empDetail(@PathVariable Long id){
        return employeeService.employeeDetail(id);
    }


//    직원 정보 수정 : 비밀번호 수정만 있음.
//    해당 직원 id, 수정할 값이 들어있음.
    @PostMapping("/modify")
    @ResponseBody
    public String userModify(@RequestBody EmployeeModResDto dto){
        employeeService.employeeModify(dto);
        return "수정 완료";
    }

//    직원의 직급을 수정
//    직원 수정은 office 부서만 가능
//    dto 안에 수정하는 직원 id, 수정 대상 id, 수정할 값 이 들어있다.
    @PostMapping("/mod_rank")
    @ResponseBody
    public String modEmployeeRank(@RequestBody EmployeeRankModResDto dto){
        employeeService.modEmployeeRank(dto);
        return "수정완료";
    }
}