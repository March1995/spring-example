package com.wyb.cache;

import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.CacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcher丶
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisDataTypeTests {

    @Resource
    private CacheService redisService;
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testHash() {
        // 存储所有key
        UserDo userDo;
        for (int i = 0; i < 10; i++) {
            userDo = new UserDo();
            redisService.sadd("list", i);
            userDo.setId(i);
            userDo.setUsername("第" + i + "个用户");
            addUser(userDo);
        }
        System.out.println(getUser(1).toString());

        for (UserDo userDo1 : getUserList()) {
            System.out.println(userDo1.getUsername());
        }
    }


    @Test
    public void pipeline() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 100000; i++ ) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
        long time1 = System.currentTimeMillis();
        RedisSerializer serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            map.forEach((key, value) -> {
                connection.set(serializer.serialize("user:" + key), serializer.serialize(JSON.toJSONString(value)));
            });
            return null;
        }, serializer);

        long time2 = System.currentTimeMillis();
        System.out.println("耗时："+ (time2 - time1));
    }

    @Test
    public void lua() {
        Long result = null;
        try {
            //调用lua脚本并执行
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setResultType(Long.class);//返回类型是Long
            //lua文件存放在resources目录下的redis文件夹内
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/redis_lock.lua")));
            result = (Long) redisTemplate.execute(redisScript, Arrays.asList("product_stock_10016"), 1);
            System.out.println("lock==" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addUser(UserDo userDo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(userDo.getId()));
        map.put("username", userDo.getUsername());
        redisService.hputs("user:" + userDo.getId(), map);
    }

    private UserDo getUser(Integer uId) {
        Map<Object, Object> map = redisService.hgetAll("user:" + uId);
        UserDo userDo = new UserDo();
        userDo.setId(Integer.parseInt(map.get("id").toString()));
        userDo.setUsername((String) map.get("username"));
        return userDo;
    }

    private List<UserDo> getUserList() {
        List<UserDo> list = new ArrayList<>();
        for (Object id : redisService.sget("list")) {
            Map<Object, Object> map = redisService.hgetAll("user:" + id);
            UserDo userDo = new UserDo();
            userDo.setId(Integer.parseInt(map.get("id").toString()));
            userDo.setUsername((String) map.get("username"));
            list.add(userDo);
        }
        return list;
    }

}
