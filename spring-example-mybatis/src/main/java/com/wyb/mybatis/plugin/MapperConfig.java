package com.wyb.mybatis.plugin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcher丶
 * @date 2022-08-24
 **/
@Configuration
//@MapperScan({"com.example.demo.mapper"})
public class MapperConfig {

    @Bean
    public MyPagePlugin myPagePlugin() {
        return new MyPagePlugin();
    }
}