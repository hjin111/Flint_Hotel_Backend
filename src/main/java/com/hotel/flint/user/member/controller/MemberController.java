package com.hotel.flint.user.member.controller;

import com.hotel.flint.common.auth.JwtAuthFilter;
import com.hotel.flint.common.auth.JwtTokenProvider;
import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.dto.MemberSignUpDto;
import com.hotel.flint.user.member.dto.MemberDetResDto;
import com.hotel.flint.user.member.dto.MemberModResDto;
import com.hotel.flint.user.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, JwtAuthFilter jwtAuthFilter) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @GetMapping("/findemail")
    public ResponseEntity<?> findEmail(@RequestBody Map<String, String> request) {
        try {
            String memberEmail = memberService.findEmail(request.get("phoneNumber"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회에 성공하였습니다.",
                    "회원님의 이메일은 " + memberEmail + "입니다");
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> memberSignUp(@RequestBody MemberSignUpDto dto) {
        try {
            Member member = memberService.memberSignUp(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,
                    member.getLastName() + member.getFirstName() + "님, 회원 가입을 축하합니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody UserLoginDto dto) {
        try {
            Member member = memberService.login(dto);
            String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getId());
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("token", jwtToken);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "환영합니다 " + member.getFirstName() + member.getLastName() + "님!", loginInfo);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> userDetail(@PathVariable Long id) {
        try {
            MemberDetResDto memberDetail = memberService.memberDetail(id);
            return new ResponseEntity<>(memberDetail, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> userDelete(@PathVariable Long id) {
        try {
            memberService.memberDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "삭제 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }


//    멤버 비밀번호 수정하는 기능
    @PutMapping("/modify")
    public ResponseEntity<?> userModify(@RequestBody MemberModResDto dto) {
        try {
            memberService.updatePassword(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
