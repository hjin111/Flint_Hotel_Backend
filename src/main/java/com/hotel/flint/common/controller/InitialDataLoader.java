//package com.hotel.flint.common.controller;
//
//import com.hotel.flint.user.employee.repository.EmployeeRepository;
//import com.hotel.flint.user.employee.service.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InitialDataLoader implements CommandLineRunner {
//
//    @Autowired
//    private EmployeeService employeeService;
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Override
//    public void run(String... args) throws Exception{
//        if (employeeRepository.findByEmail("flint@admin.com").isEmpty()) {
//            memberService.memberCreate(MemberSaveReqDto.builder()
//                    .name("admin")
//                    .email("admin@test.com")
//                    .password("12341234")
//                    .role(Role.ADMIN)
//                    .build());
//        }
//    }
//}
