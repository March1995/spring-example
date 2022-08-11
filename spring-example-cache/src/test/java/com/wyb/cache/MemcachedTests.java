package com.wyb.cache;

import com.wyb.cache.config.MemcachedConfig;
import com.wyb.cache.service.CacheService;
import net.spy.memcached.MemcachedClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemcachedTests {

    @Resource
    private MemcachedConfig memcachedConfig;
    @Resource
    private CacheService memcachedService;


    @Test
    public void testSetGet() {
        MemcachedClient memcachedClient = memcachedConfig.client();
        memcachedClient.set("testkey", 1000, "666666");
        System.out.println("***********  " + memcachedClient.get("testkey").toString());
    }

    @Test
    public void testRedisSet() {
        memcachedService.putCache("11", "11 value");
        System.out.println(memcachedService.getCache("11"));
    }

}
