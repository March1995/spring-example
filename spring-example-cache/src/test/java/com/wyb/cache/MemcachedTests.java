package com.wyb.cache;

import com.wyb.cache.config.MemcacheConfig;
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
    private MemcacheConfig memcacheConfig;
    @Resource
    private CacheService memcacheService;


    @Test
    public void testSetGet() {
        MemcachedClient memcachedClient = memcacheConfig.client();
        memcachedClient.set("testkey", 1000, "666666");
        System.out.println("***********  " + memcachedClient.get("testkey").toString());
    }

    @Test
    public void testRedisSet() {
        memcacheService.putCache("11", "11 value");
        System.out.println(memcacheService.getCache("11"));
    }

}
