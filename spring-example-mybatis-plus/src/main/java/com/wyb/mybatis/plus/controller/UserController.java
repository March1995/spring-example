package com.wyb.mybatis.plus.controller;


import com.wyb.mybatis.plus.entity.User;
import com.wyb.mybatis.plus.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Marcher丶
 * @since 2021-06-07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @GetMapping("/getUser")
    @ResponseBody
    public User getUser() {
        return userService.selectById(1);
    }
}
