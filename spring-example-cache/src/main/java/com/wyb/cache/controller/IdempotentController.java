package com.wyb.cache.controller;

import com.wyb.cache.annotation.ApiIdempotentAnn;
import com.wyb.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 幂等测试控制器
 *
 * @author Marcher丶
 * @date 2022-08-11
 **/
@RestController
@RequestMapping("/cache")
@Slf4j
public class IdempotentController {

    @Resource
    CacheService cacheService;

    private static int count = 0;

    /**
     * 获取token
     *
     * @return Token
     */
    @GetMapping("/getToken")
    public String getToken() {
        String token = UUID.randomUUID().toString().substring(1, 9);
        cacheService.putCache(token, "1", 15);
        return token;
    }

    @GetMapping("/idempotent")
    @ApiIdempotentAnn
    public void idempotent() {
        log.info("idempotent count: {}", count++);
    }
}
