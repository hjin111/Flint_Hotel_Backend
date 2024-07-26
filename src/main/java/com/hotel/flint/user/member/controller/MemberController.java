package com.hotel.flint.user.member.controller;

import com.hotel.flint.user.member.dto.MemberDetResDto;
import com.hotel.flint.user.member.dto.MemberModResDto;
import com.hotel.flint.user.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/detail/{id}")
    @ResponseBody
    public MemberDetResDto userDetail(@PathVariable Long id){
        return memberService.memberDetail(id);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String userDelete(@PathVariable Long id){
        memberService.memberDelete(id);

        return "삭제 완료";
    }

    @PostMapping("/modify")
    @ResponseBody
    public String userModify(@RequestBody MemberModResDto dto){
        memberService.memberModify(dto);
        return "수정 완료";
    }
}