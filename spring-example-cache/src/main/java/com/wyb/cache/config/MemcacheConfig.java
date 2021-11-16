package com.wyb.cache.config;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Marcher
 */
@Slf4j
@Configuration
public class MemcacheConfig {

    @Resource
    private MemcacheSource memcacheSource;

    @ConditionalOnClass(MemcachedClientFactoryBean.class)
    public MemcachedClient client() {
        MemcachedClient client = null;
        try {
            client = new MemcachedClient(new InetSocketAddress(memcacheSource.getIp(), memcacheSource.getPort()));
        } catch (IOException e) {
            log.error("inint MemcachedClient failed ", e);
        }
        if (null == client) log.error("MemcachedClient is null");
        return client;
    }
}
