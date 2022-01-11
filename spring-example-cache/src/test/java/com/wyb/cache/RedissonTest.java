/*
 * @(#)RedissonTest    Created on 2022/1/11
 * Copyright (c) 2022 ZDSoft Networks, Inc. All rights reserved.
 * $$ Id$$
 */
package com.wyb.cache;

import com.wyb.cache.lock.RedissonLocker;
import com.wyb.cache.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTest {

    private static final String KEY = "LOCK_KEY";

    @Autowired
    private RedissonLocker lockUtil;
    @Autowired
    @Qualifier("redisService")
    private CacheService redisUtil;

    @Test
    public void test() {
        //加锁
        lockUtil.lock(KEY, TimeUnit.SECONDS, 30);
        try {
            //TODO 处理业务
            System.out.println(" 处理业务。。。");
        } catch (Exception e) {
            //异常处理
        } finally {
            //释放锁
            lockUtil.unlock(KEY);
        }
        System.out.println("success");
    }


    /*
     *   hash实现购物车
     **/
    @Test
    public void testcar() {
        String key = "carUser:123456";
        redisUtil.removeCache(key);
        Map map = new HashMap();
        map.put("book:a11111", 1);
        map.put("book:a11112", 2);
        map.put("book:a11113", 3);
        redisUtil.hputs(key, map);
        System.out.println("key = " + redisUtil.hgetAll(key));
        //增加book:a11111的数量
        redisUtil.hincr(key, "book:a11111", 1);
        System.out.println(redisUtil.hgetAll(key));
        //减少book:a11112的数量
        redisUtil.hincr(key, "book:a11112", -3);
        //或者redisUtil.hdecr(key,"book:a11111",1);
        System.out.println(redisUtil.hgetAll(key));
        //获取所有key1的field的值
        System.out.println("hegetall=" + redisUtil.hgetAll(key));
        //获取key下面的map数量
        System.out.println("length=" + redisUtil.hsize(key));
        //删除某个key下的map
        redisUtil.hrem(key, "book:a11112");
        System.out.println(redisUtil.hgetAll(key));
    }


}
