package com.hotel.flint.employee.service;

import com.hotel.flint.employee.domain.Employee;
import com.hotel.flint.employee.dto.EmployeeDetResDto;
import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.employee.repository.EmployeeRepository;
import com.hotel.flint.user.domain.User;
import com.hotel.flint.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, UserService userService){
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }

    public InfoUserResDto memberInfo(Long id){
        User user = userService.findByUserId(id);
        return user.infoUserEntity();
    }

    public EmployeeDetResDto employeeDetail(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        return employee.EmpDetEntity();
    }

}
