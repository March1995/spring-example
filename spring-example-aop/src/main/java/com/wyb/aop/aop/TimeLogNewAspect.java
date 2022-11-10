package com.wyb.aop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kunzite
 */
@Aspect
@Component
public class TimeLogNewAspect {

    private static Logger log = LoggerFactory.getLogger(TimeLogNewAspect.class);

    // 创建以线程ID为key value为对应方法开始时间
    private Map<Long, Map<String, List<Long>>> threadMap = new ConcurrentHashMap<>(200);

    @Pointcut("@annotation(com.wyb.aop.annotation.TimeLog)")
    public void catchAll() {
    }

    /**
     * 环绕通知：目标方法调用前后执行的通知，可以在方法调用前后完成自定义的行为。
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("catchAll()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        // 调用方法的参数
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(signature.getDeclaringTypeName() + signature.getName());
        // 执行完方法的返回值
        // 调用proceed()方法，就会触发切入点方法执行
        Object result = pjp.proceed();
        stopWatch.stop();
        log.debug("类{},方法{} 耗时:{}毫秒", stopWatch.getLastTaskInfo().getClass(), stopWatch.getLastTaskInfo().getTaskName(), stopWatch.getLastTaskTimeMillis());
        return result;
    }
}
