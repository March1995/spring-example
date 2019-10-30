package com.wyb.vue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wyb.vue.repository")
public class SpringExampleVueApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringExampleVueApplication.class, args);
    }

}
