package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.room.domain.RoomInfo;
import com.hotel.flint.reserve.room.repository.RoomDetailsRepository;
import com.hotel.flint.reserve.room.repository.RoomInfoRepositoy;
import com.hotel.flint.reserve.room.repository.RoomPriceRepository;
import com.hotel.flint.user.employee.dto.Employee;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EmployeeRoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final RoomInfoRepositoy roomInfoRepositoy;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeRoomService(RoomPriceRepository roomPriceRepository, RoomDetailsRepository roomDetailsRepository, RoomInfoRepositoy roomInfoRepositoy, EmployeeRepository employeeRepository) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomInfoRepositoy = roomInfoRepositoy;
        this.employeeRepository = employeeRepository;
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

    public void modRoomPrice(Long id, Double newPrice){
        Employee authenticatedEmployee = getAuthenticatedEmployee();

        if(!authenticatedEmployee.getDepartment().toString().equals("ROOM")){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        RoomInfo roomInfo = roomInfoRepositoy.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("해당 타입의 객실은 존재하지 않습니다."));

        roomInfo.updateRoomPrice(newPrice);

    }

}
