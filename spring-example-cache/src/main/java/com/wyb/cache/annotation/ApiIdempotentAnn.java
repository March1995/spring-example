package com.wyb.cache.annotation;

import java.lang.annotation.*;

/**
 * 幂等注解
 *
 * @author Marcher丶
 * @date 2022-08-11
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiIdempotentAnn {
    boolean value() default true;
}
