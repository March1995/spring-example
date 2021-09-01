package com.wyb.mybatis.controller;

import com.wyb.mybatis.converter.UserConverter;
import com.wyb.mybatis.dao.model.UserDo;
import com.wyb.mybatis.dao.model.UserExDo;
import com.wyb.mybatis.dto.UserDTO;
import com.wyb.mybatis.service.UserExService;
import com.wyb.mybatis.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Marcher丶
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserExService userExService;
    @Autowired
    UserConverter userConverter;


    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public List<UserDo> list() {
        return userService.selectAll();
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping("/getById")
    public UserDTO getById(@RequestParam Long id) {
        return userConverter.item2Dto(userService.selectByKey(id));
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public int add(@RequestBody UserDo userDo) {
        return userService.save(userDo);
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/updateUser")
    public void update(@RequestBody UserDo userDo) {
        userService.update(userDo);

    }

    @ApiOperation(value = "修改用户EX")
    @PostMapping("/updateUserEx")
    public void updateEx(@RequestBody UserExDo userExDo) {
        userExService.update(userExDo);

    }

    @PostMapping("/login")
    public boolean login(@RequestParam("username") String name,
                         @RequestParam("pwd") String pwd) {
        if (name.equals("111111") && pwd.equals("123456")) {
            log.info("登录请求");
            return true;
        }
        return false;

    }
}
