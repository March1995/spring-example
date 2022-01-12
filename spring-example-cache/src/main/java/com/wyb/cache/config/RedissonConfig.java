package com.wyb.cache.config;

import com.wyb.cache.lock.RedissonLocker;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // cluster
        if (redisProperties.getCluster() != null) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            clusterServersConfig.setScanInterval(2000); // 集群状态扫描间隔时间，单位是毫秒
            List<String> nodeList = redisProperties.getCluster().getNodes();
            String[] nodes = new String[nodeList.size()];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = "redis://" + nodeList.get(i);
            }
            clusterServersConfig.addNodeAddress(nodes);
            if (redisProperties.getPassword() != null) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        }
        //sentinel
        else if (redisProperties.getSentinel() != null) {
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
            List<String> nodeList = redisProperties.getCluster().getNodes();
            String[] nodes = new String[nodeList.size()];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = "redis://" + nodeList.get(i);
            }
            sentinelServersConfig.addSentinelAddress(nodes);
            sentinelServersConfig.setDatabase(redisProperties.getDatabase());
            if (redisProperties.getPassword() != null) {
                sentinelServersConfig.setPassword(redisProperties.getPassword());
            }
        } else { //single server
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
            singleServerConfig.setDatabase(redisProperties.getDatabase());
            if (redisProperties.getPassword() != null) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
        }
        return Redisson.create(config);
    }

    @Bean
    public RedissonLocker redissonLocker(RedissonClient redissonClient) {
        RedissonLocker locker = new RedissonLocker(redissonClient);
        //设置LockUtil的锁处理对象
//        LockUtil.setLocker(locker);
        return locker;
    }
}
