package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.dto.*;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import com.hotel.flint.user.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final DiningReservationRepository diningReservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           MemberRepository memberRepository,
                           MemberService memberService,
                           DiningReservationRepository diningReservationRepository) {
        this.employeeRepository = employeeRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.diningReservationRepository = diningReservationRepository;
    }

    private Employee getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            return employeeRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }

//    직원 생성(Office 부서만 가능함)
    public Employee makeEmployee(EmployeeMakeDto dto) {
        Employee authenticatedEmployee = getAuthenticatedEmployee();
        if(!authenticatedEmployee.getDepartment().toString().equals("Office")){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        if (employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent() ||
            memberRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).isPresent()) {
            throw new IllegalArgumentException("해당 이메일로 이미 가입한 계정이 존재합니다.");
        }
        if (employeeRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent() ||
            memberRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), Option.N).isPresent()) {
            throw new IllegalArgumentException("해당 번호로 이미 가입한 계정이 존재합니다");
        }
        return employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
    }

//    직원 로그인
    public Employee login(UserLoginDto dto) {
        Employee employee = employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).orElseThrow(
            () -> new EntityNotFoundException("아이디가 틀렸습니다."));
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return employee;
    }

    public String findEmail(String phoneNumber){
        Employee employee = employeeRepository.findByPhoneNumberAndDelYN(phoneNumber, Option.N).orElseThrow(
                ()-> new EntityNotFoundException("해당 번호로 가입한 계정이 없습니다. 관리자에게 문의해주세요."));
        return employee.getEmail();
    }

    public InfoUserResDto memberInfo(Long id) {
        Member member = memberService.findByUserId(id);
        return member.infoUserEntity();
    }

//    직원 상세 정보
    public EmployeeDetResDto employeeDetail(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        return employee.EmpDetEntity();
    }

//    직원 계정 비밀번호 수정
    public void employeeModify(EmployeeModResDto dto){
        Employee employee = this.findByEmpId(dto.getId());
        employee.modifyEmp(dto.getPassword());
        employeeRepository.save(employee);
    }


//    직원 ID 찾는 로직
    public Employee findByEmpId(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
    }

//    직원 직급 수정 로직
    public Employee modEmployeeRank(EmployeeRankModResDto dto){
        Employee officeEmployee = employeeRepository.findById(dto.getOfficeId()).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        if(!officeEmployee.getDepartment().equals(Department.Office))
            throw new IllegalArgumentException("Office 부서만 수정이 가능합니다.");
        Employee targetEmployee = employeeRepository.findById(dto.getTargetId()).orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        targetEmployee.modifyRank(dto.getEmployeeRank());
        return employeeRepository.save(targetEmployee);
    }

    public void delAccount(Long id){
        Employee authenticatedEmployee = getAuthenticatedEmployee();
        if(!authenticatedEmployee.getDepartment().toString().equals("Office")){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        Employee delEmp = employeeRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 직원입니다."));
        delEmp.delEmp();
    }

}
