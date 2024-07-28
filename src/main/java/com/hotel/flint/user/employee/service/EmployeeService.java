package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.enumdir.DepartMent;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import com.hotel.flint.user.employee.dto.Employee;
import com.hotel.flint.user.employee.dto.EmployeeDetResDto;
import com.hotel.flint.user.employee.dto.EmployeeMakeDto;
import com.hotel.flint.user.employee.dto.EmployeeModResDto;
import com.hotel.flint.user.employee.dto.EmployeeRankModResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import com.hotel.flint.user.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Employee makeEmployee(EmployeeMakeDto dto) {
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

    public Employee login(UserLoginDto dto) {
        Employee employee = employeeRepository.findByEmailAndDelYN(dto.getEmail(), Option.N).orElseThrow(
            () -> new EntityNotFoundException("아이디가 틀렸습니다."));
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return employee;
    }

    public InfoUserResDto memberInfo(Long id) {
        Member member = memberService.findByUserId(id);
        return member.infoUserEntity();
    }

    public EmployeeDetResDto employeeDetail(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        return employee.EmpDetEntity();
    }

    public void employeeModify(EmployeeModResDto dto) {
        Employee employee = this.findByEmpId(dto.getId());
        employee.modifyEmp(dto.getPassword());
        employeeRepository.save(employee);
    }

    public Employee findByEmpId(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
    }

    public Employee modEmployeeRank(EmployeeRankModResDto dto) {
        Employee officeEmployee = employeeRepository.findById(dto.getOfficeId()).orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        if (!officeEmployee.getDepartment().equals(DepartMent.Office))
            throw new IllegalArgumentException("Office 부서만 수정이 가능합니다.");
        Employee targetEmployee = employeeRepository.findById(dto.getTargetId()).orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        targetEmployee.modifyRank(dto.getEmployeeRank());
        return employeeRepository.save(targetEmployee);
    }
}
