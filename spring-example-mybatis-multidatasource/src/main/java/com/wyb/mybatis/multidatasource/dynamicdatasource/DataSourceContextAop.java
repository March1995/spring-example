package com.wyb.mybatis.multidatasource.dynamicdatasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcher丶
 */
@Slf4j
@Aspect
@Order(value = 1)
@Component
public class DataSourceContextAop {

    @Around("@annotation(com.wyb.mybatis.multidatasource.dynamicdatasource.DataSourceSwitcher)")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {
        boolean clear = false;
        try {
            Method method = this.getMethod(pjp);
            DataSourceSwitcher dataSourceSwitcher = method.getAnnotation(DataSourceSwitcher.class);
            clear = dataSourceSwitcher.clear();
            DataSourceContextHolder.set(dataSourceSwitcher.value().getDataSourceName());
            log.info("数据源切换至：{}", dataSourceSwitcher.value().getDataSourceName());
            return pjp.proceed();
        }
        finally {
            if (clear) {
                DataSourceContextHolder.clear();
            }

        }
    }

    private Method getMethod(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }
}
