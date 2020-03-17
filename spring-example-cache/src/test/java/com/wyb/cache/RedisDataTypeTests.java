package com.wyb.cache;

import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcher丶
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisDataTypeTests {

    @Resource
    private CacheService redisService;

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
    public void checkProcessOrderStatus() {
//        for (int i = 0; i < 10; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                }
//            }).start();
//        }
        try {
            boolean t = redisService.tryLock("LipapayOrderQueryScheduled.checkProcessOrderStatus",
                    2000, 2000);
            System.out.println("线程：" + Thread.currentThread().getName() + "获取锁" + (t ? "成功" : "失败"));

            redisService.unlock( "LipapayOrderQueryScheduled.checkProcessOrderStatus");
        } catch (Exception e) {
            log.error("[LipapayOrderQueryScheduled][checkProcessOrderStatus] process error, e {}", e);
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