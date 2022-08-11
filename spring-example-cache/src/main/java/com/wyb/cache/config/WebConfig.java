package com.wyb.cache.config;

import com.wyb.cache.intercepter.ApiIdempotentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Marcher丶
 * @date 2022-08-11
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    ApiIdempotentInterceptor apiIdempotentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.apiIdempotentInterceptor);
    }
}
