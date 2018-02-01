package com.wyb.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Kunzite
 */
@EnableCaching
@SpringBootApplication
@MapperScan(basePackages = "com.wyb.cache.dao.mapper")
@ImportResource({"classpath*:spring/spring-redis.xml"})
public class SpringExampleCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringExampleCacheApplication.class, args);
	}
}
