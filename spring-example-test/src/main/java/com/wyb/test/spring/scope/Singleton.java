package com.wyb.test.spring.scope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Marcher丶
 * 单例
 */
@SpringBootApplication
@Controller
public class Singleton {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Singleton.class);

        ScopeBean singleTonBean1 = (ScopeBean) applicationContext.getBean("singletonBean");
        int sAddr1 = System.identityHashCode(singleTonBean1);
        ScopeBean singleTonBean2 = (ScopeBean) applicationContext.getBean("singletonBean");
        int sAddr2 = System.identityHashCode(singleTonBean2);

        System.out.println("第一次获取单例bean内存地址：" + sAddr1);
        System.out.println("第二次获取单例bean内存地址：" + sAddr2);
        System.out.println("第一次与第二是否为同一个对象：" + (sAddr1 == sAddr2));


        ScopeBean prototypeBean1 = (ScopeBean) applicationContext.getBean("prototypeBean");
        ScopeBean prototypeBean2 = (ScopeBean) applicationContext.getBean("prototypeBean");
        int pAddr1 = System.identityHashCode(prototypeBean1);
        int pAddr2 = System.identityHashCode(prototypeBean2);

        System.out.println("第一次获取多例bean内存地址：" + pAddr1);
        System.out.println("第二次获取多例bean内存地址：" + pAddr1);
        System.out.println("第一次与第二是否为同一个对象：" + (pAddr1 == pAddr2));

        applicationContext.close();

        SpringApplication.run(Singleton.class, args);


//        ((ClassPathXmlApplicationContext) applicationContext).close();
    }

    @GetMapping("/test")
    @ResponseBody
    public void test(HttpServletRequest request) {
        ServletContext sc = request.getSession().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);

        ScopeBean requestBean = (ScopeBean) ctx.getBean("requestBean");
        System.out.println("test requestBean1内存地址：" + System.identityHashCode(requestBean));
        ScopeBean requestBean2 = (ScopeBean) ctx.getBean("requestBean");
        System.out.println("test requestBean2内存地址：" + System.identityHashCode(requestBean2));
    }

    @GetMapping("/test1")
    @ResponseBody
    public void test1(HttpServletRequest request) {
        ServletContext sc = request.getSession().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
        ScopeBean requestBean = (ScopeBean) ctx.getBean("requestBean");
        System.out.println("test1 requestBean内存地址：" + System.identityHashCode(requestBean));
    }

    /**
     * key只存在于一级域名  写在一级域名 二级域名读
     * key 一二级域名都有  读有问题
     * @param request
     * @param response
     */
    @GetMapping("/addCookie")
    public void addCookie(HttpServletRequest request, HttpServletResponse response) {
        String domain = "dev.kehou.com";
        String name = "name";
        String value = "value";
        CookieUtil.addCookieByName(request, response, domain, name, value, 30 * 10);
        CookieUtil.addCookieByName(request, response, "m.dev.kehou.com", name, "mvalue", 30 * 10);
        value = CookieUtil.getCookieValueByName(request, name);
        System.out.println(value);
//        String
    }

    @GetMapping("/getCookie")
    public void getCookie(HttpServletRequest request, HttpServletResponse response) {
        String name = "name";
        String value = CookieUtil.getCookieValueByName(request, name);
        System.out.println(value);
    }

    @GetMapping("/updateCookie")
    public void updateCookie(HttpServletRequest request, HttpServletResponse response) {
        String name = "name";
        String value = "valueUpdate";

        String domain = "dev.kehou.com";
        CookieUtil.addCookieByName(request, response, domain, name, value, 30 * 10);
        value = CookieUtil.getCookieValueByName(request, name);
        System.out.println(value);
    }
}
