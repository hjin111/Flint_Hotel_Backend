package com.hotel.flint.user.controller;

import com.hotel.flint.user.dto.UserDetResDto;
import com.hotel.flint.user.dto.UserModResDto;
import com.hotel.flint.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/member")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping("/detail/{id}")
    @ResponseBody
    public UserDetResDto userDetail(@PathVariable Long id){
        return userService.memberDetail(id);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String userDelete(@PathVariable Long id){
        userService.memberDelete(id);

        return "삭제 완료";
    }

    @PostMapping("/modify")
    @ResponseBody
    public String userModify(@RequestBody UserModResDto dto){
        userService.memberModify(dto);
        return "수정 완료";
    }
}
