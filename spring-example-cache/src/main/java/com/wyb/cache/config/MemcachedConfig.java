package com.wyb.cache.config;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Marcher
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MemcachedSource.class)
public class MemcachedConfig {

    @Resource
    private MemcachedSource memcachedSource;

    @ConditionalOnClass(MemcachedClientFactoryBean.class)
    public MemcachedClient client() {
        MemcachedClient client = null;
        try {
            client = new MemcachedClient(new InetSocketAddress(memcachedSource.getIp(), memcachedSource.getPort()));
        } catch (IOException e) {
            log.error("init MemcachedClient failed ", e);
        }
        if (null == client) log.error("MemcachedClient is null");
        return client;
    }
}
