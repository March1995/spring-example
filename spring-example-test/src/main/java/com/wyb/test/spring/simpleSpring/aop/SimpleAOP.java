package com.wyb.test.spring.simpleSpring.aop;

import java.lang.reflect.Proxy;

/**
 * @author Marcher丶
 * 代理生成类
 */
public class SimpleAOP {

    // 生成代理对象
    public static Object getProxyObject(Object object, Advice advice) {
        return Proxy.newProxyInstance(advice.getClass().getClassLoader(), object
                .getClass().getInterfaces(), advice);
    }
}
