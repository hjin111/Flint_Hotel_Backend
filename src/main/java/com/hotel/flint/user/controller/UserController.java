package com.hotel.flint.user.controller;

import com.hotel.flint.user.domain.User;
import com.hotel.flint.user.dto.UserdetResDto;
import com.hotel.flint.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public UserdetResDto userDetail(@PathVariable Long id){
        return userService.memberDetail(id);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String userDelete(@PathVariable Long id){
        userService.memberDelete(id);

        return "삭제 완료";
    }
}
