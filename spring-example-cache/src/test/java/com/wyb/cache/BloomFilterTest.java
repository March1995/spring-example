package com.wyb.cache;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.wyb.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BloomFilterTest {

    @Resource
    private CacheService redisService;
    @Resource
    private RedisTemplate redisTemplate;

    BloomFilter<String> bloomFilter;

    @Before
    public void initBloomFilter() {
        Set<String> keys = redisTemplate.keys("user:*");
        //        1000：期望存入的数据个数，0.001：期望的误差率 5
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 1000, 0.001);
        //把所有数据存入布隆过滤器
        for (String key : keys) {
            bloomFilter.put(key);
        }
    }

    @Test
    public void bloomFilter() {
        String key = "user:1";
        // 从布隆过滤器这一级缓存判断下key是否存在
        Boolean exist = bloomFilter.mightContain(key);
        if (!exist) {
            return;
        }
        // 从缓存中获取数据
        String cacheValue = (String) redisService.getCache(key);
        // 缓存为空
        if (StringUtils.isBlank(cacheValue)) {  // 从存储中获取
            String storageValue = (String) redisService.getCache(key);
            redisService.putCache(key, storageValue);
            // 如果存储数据为空， 需要设置一个过期时间(300秒)
            if (storageValue == null) {
                redisService.expire(key, 60 * 5, TimeUnit.SECONDS);
            }
            System.out.println(storageValue);
        } else {
            System.out.println(cacheValue);
        }
    }

}
