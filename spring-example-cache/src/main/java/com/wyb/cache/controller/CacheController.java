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
 * @version: 2018-02-01 15:57
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
        }
        else {
            redisService.putCache(key, value);
        }
        return redisService.getCache(key);
    }

    @GetMapping("/get")
    public UserDo getById() {
        return (UserDo) redisService.getCache("1");
    }

    @GetMapping("/lock")
    public String lock() {
        try {
            boolean t = redisService.tryLock("LipapayOrderQueryScheduled.checkProcessOrderStatus", 2000, 2000);
            String s = Thread.currentThread().getName() + "=====================";
            if (!t) {
                return "服务繁忙，请退出重试";
            }
            Integer stock;
            // 拿到分布式锁 去取库存
            UserDo userDo = userService.getById(1);
            if ((stock = userDo.getAge()) <= 0) {
                System.out.println("下单失败，已经没有库存了");
                return "下单失败，已经没有库存了";
            }
            stock--;
            userService.updateAgeById(stock, userDo.getId());
            System.out.println(s + "下单成功，当前剩余产品数：" + stock);
            return "下单成功，当前剩余产品数：" + stock;
        }
        catch (Exception e) {
            log.error("[LipapayOrderQueryScheduled][checkProcessOrderStatus] process error, e {}", e);
        }
        finally {
            redisService.unlock("LipapayOrderQueryScheduled.checkProcessOrderStatus");
        }
        return "服务繁忙，请退出重试";
    }
}
