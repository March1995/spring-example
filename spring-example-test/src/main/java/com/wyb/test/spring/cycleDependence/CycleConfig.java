package com.wyb.test.spring.cycleDependence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;

/**
 * @author Marcher丶
 * <p>
 * 构造器注入存在循环依赖的问题 使用@lazy进行懒加载 循环依赖的类都需要加
 * setter方式比较循环依赖
 */
@Configuration
//@ImportResource("classpath:springCycleDI.xml")
public class CycleConfig {

//    @Bean
//    @Lazy
//    public CdA CdA(CdB cdB) {
//        return new CdA(cdB);
//    }
//
//    @Bean
//    @Lazy
//    public CdB CdB(CdA cdA) {
//        return new CdB(cdA);
//    }

    @Bean
    public CdA CdA(CdB cdB) {
        CdA cdA = new CdA();
        cdA.setCdB(cdB);
        return cdA;
    }

    @Bean
    public CdB CdB(CdA cdA) {
        CdB cdB = new CdB();
        cdB.setCdA(cdA);
        return cdB;
    }

}
