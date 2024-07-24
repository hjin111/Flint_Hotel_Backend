package com.hotel.flint.employee.service;

import com.hotel.flint.employee.domain.Employee;
import com.hotel.flint.employee.dto.EmployeeDetResDto;
import com.hotel.flint.employee.dto.EmployeeModResDto;
import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.employee.repository.EmployeeRepository;
import com.hotel.flint.member.domain.Member;
import com.hotel.flint.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MemberService memberService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MemberService memberService){
        this.employeeRepository = employeeRepository;
        this.memberService = memberService;
    }

    public InfoUserResDto memberInfo(Long id){
        Member member = memberService.findByUserId(id);
        return member.infoUserEntity();
    }

    public EmployeeDetResDto employeeDetail(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        return employee.EmpDetEntity();
    }

    public void employeeModify(EmployeeModResDto dto){
        Employee employee = this.findByEmpId(dto.getId());
        employee.modifyEmp(dto.getPassword());
        employeeRepository.save(employee);
    }

    public Employee findByEmpId(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
    }
}
