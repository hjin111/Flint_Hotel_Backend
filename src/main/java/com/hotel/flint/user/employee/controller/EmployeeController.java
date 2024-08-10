package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.auth.JwtAuthFilter;
import com.hotel.flint.common.auth.JwtTokenProvider;
import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.dto.FindPasswordRequest;
import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.service.MailService;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.dto.EmployeeMakeDto;
import com.hotel.flint.user.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    MailService mailService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, JwtTokenProvider jwtTokenProvider, JwtAuthFilter jwtAuthFilter) {
        this.employeeService = employeeService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @PostMapping("/create")
    public ResponseEntity<?> makeEmployee(@RequestBody EmployeeMakeDto dto){
        try {
            Employee employee = employeeService.makeEmployee(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "계정 생성이 성공적으로 완료되었습니다",null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody UserLoginDto dto){
        try {
            Employee employee = employeeService.login(dto);
            String jwtToken = jwtTokenProvider.createEmployeeToken(employee.getEmail(), employee.getId(), employee.getDepartment().toString());
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("employeetoken", jwtToken);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "환영합니다 " + employee.getFirstName() + employee.getLastName() + "님!", loginInfo);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findemail")
    public ResponseEntity<?> findEmail(@RequestBody Map<String, String> request) {
        try {
            String Email = employeeService.findEmailToPhoneNum(request.get("phoneNumber"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회에 성공하였습니다.",
                    "회원님의 이메일은 " + Email + "입니다");
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/findpassword")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequest request) {
        try {
            mailService.sendTempPassword(request.getEmail());
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "임시 비밀번호를 이메일로 발송했습니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/delaccount")
    public ResponseEntity<?> delEmployee(@RequestBody Map<String, Long> request){
        try {
            employeeService.delAccount(request.get("employeeId"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "퇴사 처리 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
