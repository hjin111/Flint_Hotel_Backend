package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.Employee;
import com.hotel.flint.user.employee.dto.EmployeeMakeDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MemberRepository memberRepository) {
        this.employeeRepository = employeeRepository;
        this.memberRepository = memberRepository;
    }

    private Employee getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("인증 값::" + authentication + "\n 여기가 끝");
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            return employeeRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }

    public Employee makeEmployee(EmployeeMakeDto dto){
        Employee authenticatedEmployee = getAuthenticatedEmployee();
        if(!authenticatedEmployee.getDepartment().toString().equals("OFFICE")){
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        if(employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent() ||
                memberRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent()){
            throw new IllegalArgumentException("해당 이메일로 이미 가입한 계정이 존재합니다.");
        }
        if(employeeRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent() ||
                memberRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent()){
            throw new IllegalArgumentException("해당 번호로 이미 가입한 계정이 존재합니다");
        }
        return employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
    }


    public Employee login(UserLoginDto dto){
        Employee employee = employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).orElseThrow(
                ()-> new EntityNotFoundException("아이디가 틀렸습니다."));
        if(!passwordEncoder.matches(dto.getPassword(), employee.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return employee;
    }
}
