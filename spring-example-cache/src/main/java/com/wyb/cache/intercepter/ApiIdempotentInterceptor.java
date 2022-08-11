package com.wyb.cache.intercepter;

import com.wyb.cache.annotation.ApiIdempotentAnn;
import com.wyb.cache.service.CacheService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author Marcher丶
 * @date 2022-08-11
 **/
@Component
public class ApiIdempotentInterceptor extends HandlerInterceptorAdapter {

    @Resource
    CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotentAnn apiIdempotentAnn = method.getAnnotation(ApiIdempotentAnn.class);
        if (null != apiIdempotentAnn && apiIdempotentAnn.value()) {
            // 实现接口幂等性check
            boolean result = checkToken(request);
            if (!result) {
                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.print("重复调用");
                writer.close();
                response.flushBuffer();
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    private boolean checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        if (null == token || "".equals(token)) {
            token = request.getParameter("token");
            if (null == token || "".equals(token)) {
                return false;
            }
        }
        // 返回是否删除成功
        return cacheService.removeCache(token);
    }
}
