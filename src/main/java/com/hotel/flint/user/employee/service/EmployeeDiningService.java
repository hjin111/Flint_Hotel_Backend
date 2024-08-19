package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.dining.domain.Menu;
import com.hotel.flint.dining.dto.MenuSaveDto;
import com.hotel.flint.dining.repository.DiningRepository;
import com.hotel.flint.dining.repository.MenuRepository;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.ReservationDetailDto;
import com.hotel.flint.reserve.dining.repository.DiningReservationRepository;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.dto.DiningMenuDto;
import com.hotel.flint.user.employee.dto.InfoDiningResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.employee.dto.MenuSearchDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeDiningService {
    private final DiningRepository diningRepository;
    private final MenuRepository menuRepository;
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final DiningReservationRepository diningReservationRepository;
    private final EmployeeService employeeService;

    public EmployeeDiningService(DiningRepository diningRepository, MenuRepository menuRepository, EmployeeRepository employeeRepository, MemberRepository memberRepository, DiningReservationRepository diningReservationRepository, EmployeeService employeeService){
        this.diningRepository = diningRepository;
        this.menuRepository = menuRepository;
        this.employeeRepository = employeeRepository;
        this.memberRepository = memberRepository;
        this.diningReservationRepository = diningReservationRepository;
        this.employeeService = employeeService;
    }

    private Employee getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails);
            String email = userDetails.getUsername();
            return employeeRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }

    public List<DiningMenuDto> getMenuList(Department department, MenuSearchDto dto){
        department = getAuthenticatedEmployee().getDepartment();
        DiningName diningName = mapToDepartmentToDining(department);
        Dining dining = diningRepository.findByDiningName(diningName).orElseThrow(
                ()-> new EntityNotFoundException("해당 부서는 존재하지 않습니다"));

        Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                // 이름 검색 조건 추가
                if (dto.getMenuName() != null && !dto.getMenuName().isEmpty()) {
                    predicates.add(criteriaBuilder.like(root.get("menuName"), "%" + dto.getMenuName() + "%"));
                }

                // id 검색 조건 추가 - 정확히 일치하는 값으로 검색
                if (dto.getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), dto.getId()));
                }

                // 다이닝 필터링 조건 추가
                predicates.add(criteriaBuilder.equal(root.get("dining"), dining));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        List<Menu> menus = menuRepository.findAll(specification);

        List<DiningMenuDto> dtos = new ArrayList<>();
        for(Menu menu: menus){
            dtos.add(menu.fromEntity(menu));
        }

        return dtos;
    }


    private DiningName mapToDepartmentToDining(Department department){
        switch (department){
            case KorDining:
                return DiningName.KorDining;
            case JapDining:
                return DiningName.JapDining;
            case ChiDining:
                return DiningName.ChiDining;
            case Lounge:
                return DiningName.Lounge;
            case Room:
                throw new IllegalArgumentException("접근권한이 없습니다.");
            case Office:
                throw new IllegalArgumentException("접근권한이 없습니다.");
            default:
                throw new IllegalArgumentException("접근권한이 없습니다.");
        }
    }

    public void addDiningMenu(MenuSaveDto menuSaveDto){
        Employee authenticatedEmployee = getAuthenticatedEmployee();

        Dining dining = diningRepository.findById(menuSaveDto.getDiningId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Dining ID"));
        if(authenticatedEmployee.getDepartment().toString() != dining.getDiningName().toString()){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        Menu menu = menuSaveDto.toEntity(dining);
        menuRepository.save(menu);
    }

    public void modDiningMenu(Long menuId, int newCost){
        Employee authenticatedEmployee = getAuthenticatedEmployee();

        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 메뉴"));
        Dining dining = diningRepository.findById(menu.getDining().getId()).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Dining ID"));
        if(authenticatedEmployee.getDepartment().toString() != dining.getDiningName().toString()){
            System.out.println("여기 문제");
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        menu.menuUpdate(newCost);
        menuRepository.save(menu);
    }

    public void delDiningMenu(Long menuId){
        Employee authenticatedEmployee = getAuthenticatedEmployee();

        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new EntityNotFoundException("삭제하려는 메뉴가 존재하지 않습니다."));
        Dining dining = diningRepository.findById(menu.getDining().getId()).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Dining ID"));
        if(authenticatedEmployee.getDepartment().toString() != dining.getDiningName().toString()){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        menuRepository.deleteById(menuId);
    }

    public List<InfoDiningResDto> memberReservationDiningCheck(Long id){
        Employee authenticateEmployee = getAuthenticatedEmployee();
        DiningReservation diningReservation1 = diningReservationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 예약이 없습니다."));
        DiningName dining = diningReservation1.getDiningId().getDiningName();

        String auth = authenticateEmployee.getDepartment().toString();
        if(!auth.equals(dining.toString()) && !auth.equals(Department.Office.toString())){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        Member member = memberRepository.findById(diningReservation1.getMemberId().getId())
                .orElseThrow(()->new EntityNotFoundException("해당 ID의 멤버가 없습니다."));

        List<DiningReservation> diningReservation = diningReservationRepository.findByMemberId(member);
        InfoUserResDto infoUserResDto = employeeService.memberInfo(member.getEmail());
        List<InfoDiningResDto> infoDiningResDtoList = new ArrayList<>();

        for(DiningReservation revs : diningReservation){
            infoDiningResDtoList.add(revs.toInfoDiningResDto(infoUserResDto));
        }

        return infoDiningResDtoList;
    }

    public ReservationDetailDto memberReservationCncDiningByEmployee(Long id){
        DiningReservation diningReservation = diningReservationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 예약 내역이 없습니다."));
        ReservationDetailDto dto = diningReservation.fromEntity(id);
        diningReservationRepository.deleteById(id);
        return dto;
    }
}
