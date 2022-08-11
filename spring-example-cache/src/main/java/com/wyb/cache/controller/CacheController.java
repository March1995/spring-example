package com.wyb.cache.controller;

import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.CacheService;
import com.wyb.cache.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author: Marcher丶
 */
@Slf4j
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Resource
    private UserService userService;

    @Resource
    private CacheService redisService;

    @GetMapping("/addById")
    public UserDo addById() {
        UserDo userDo = userService.getById(1);
        redisService.putCache(String.valueOf(userDo.getId()), userDo);
        userDo = (UserDo) redisService.getCache(userDo.getId().toString());
        return userDo;
    }

    @GetMapping("/add")
    public Object add(String key, String value, Integer expire) {
        if (null != expire) {
            redisService.putCache(key, value, expire);
        } else {
            redisService.putCache(key, value);
        }
        return redisService.getCache(key);
    }

    @GetMapping("/get")
    public UserDo getById() {
        return (UserDo) redisService.getCache("1");
    }

}
