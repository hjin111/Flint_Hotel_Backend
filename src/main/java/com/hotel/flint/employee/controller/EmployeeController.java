package com.hotel.flint.employee.controller;

import com.hotel.flint.employee.dto.EmployeeDetResDto;
import com.hotel.flint.employee.dto.EmployeeModResDto;
import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.employee.service.EmployeeService;
import com.hotel.flint.user.dto.UserModResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping("/userinfo/{id}")
    @ResponseBody
    public InfoUserResDto memberInfoCheck(@PathVariable Long id){
        return employeeService.memberInfo(id);
    }

    @PostMapping("/detail/{id}")
    @ResponseBody
    public EmployeeDetResDto empDetail(@PathVariable Long id){
        return employeeService.employeeDetail(id);
    }

    @PostMapping("/modify")
    @ResponseBody
    public String userModify(@RequestBody EmployeeModResDto dto){
        employeeService.employeeModify(dto);
        return "수정 완료";
    }
}
