package com.wyb.cache.controller;

import com.wyb.cache.dao.mapper.UserDoMapper;
import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 缓存和数据库数据一致性策略
 *
 * @author Marcher丶
 */
@Slf4j
@RestController
@RequestMapping("/strategy")
public class CacheStrategyController {

    public static final String CACHE_ASIDE_PATTERN_KEY = "cache_aside_pattern_key:";

    @Resource
    private CacheService redisService;
    @Resource
    private UserDoMapper userDoMapper;

    /**
     * Cache Aside Pattern（旁路缓存模式）是我们平时使用比较多的一个缓存读写模式，比较适合读请求比较多的场景。
     * 取值:先取缓存，取不到去数据库查询，再放入缓存。
     */
    @GetMapping("/read")
    public void cacheAsidePatternRead() {
        String userKey = CACHE_ASIDE_PATTERN_KEY + 1;
        UserDo userDo = (UserDo) redisService.getCache(userKey);
        if (null == userDo) {
            log.info("read redis empty");
            userDo = userDoMapper.selectByPrimaryKey("1");
            // log.info("read mysql " + userDo.getUsername());
            System.out.println("read redis add " + userDo.getUsername());
            redisService.putCache(userKey, userDo);
        } else {
            System.out.println("read redis " + userDo.getUsername());
        }

    }

    /**
     * 修改:先更新数据库，再直接删除缓存
     */
    @GetMapping("/write")
    public void cacheAsidePatternWrite(String value) {
        UserDo userDo = new UserDo();
        userDo.setId(1);
        userDo.setUsername(value);
        String userKey = CACHE_ASIDE_PATTERN_KEY + 1;

        log.info("write mysql " + value);
        int o = userDoMapper.updateByPrimaryKey(userDo);
        System.out.println("write mysql over " + o);
        if (0 < o) {
            redisService.removeCache(userKey);
            log.info("write flush redis");
        }
    }
}
