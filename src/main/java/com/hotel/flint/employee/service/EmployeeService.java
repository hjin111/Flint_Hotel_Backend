package com.hotel.flint.employee.service;

import com.hotel.flint.employee.dto.InfoUserResDto;
import com.hotel.flint.employee.repository.EmployeeRepository;
import com.hotel.flint.user.domain.User;
import com.hotel.flint.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
