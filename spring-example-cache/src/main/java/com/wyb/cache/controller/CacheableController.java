package com.wyb.cache.controller;

import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * controller直接加入缓存
 *
 * @author: Kunzite
 * @Date: 2018-01-07 19:57
 */
@RestController
@RequestMapping("/user")
public class CacheableController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @Cacheable(value = "user")
    public List<UserDo> list() {
        return userService.listAll(1, 10);
    }

    @GetMapping("/getById")
    @Cacheable(value = "user", key = "#id")
    public UserDo getById(Integer id) {
        return userService.getById(id);
    }

    @PostMapping("/getByIds")
    public List<UserDo> getByIds(@RequestBody List<Integer> ids) {
        return userService.getByIds(ids);
    }

    @RequestMapping("/updateUser")
    @CachePut(value = "user", key = "#userDo.id")
    public UserDo updateUser(UserDo userDo) {
        return userService.updateUserNameById(userDo);
    }

    @RequestMapping("/updateUser1")
    @CachePut(value = "user2", key = "#userDo.username")
    public UserDo updateUser1(UserDo userDo) {
        return userService.updateUserNameById(userDo);
    }

    @RequestMapping("/removeUser")
    @CacheEvict(value = {"user"}, key = "#userDo.id")
    public String removeUser(UserDo userDo) {
        return "";
//        return userService.removeUserById(userDo) ? "删除成功" : "删除失败";
    }

}
