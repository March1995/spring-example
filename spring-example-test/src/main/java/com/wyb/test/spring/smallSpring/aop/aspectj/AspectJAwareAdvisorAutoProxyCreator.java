package com.wyb.test.spring.smallSpring.aop.aspectj;

import com.wyb.test.spring.smallSpring.aop.advisor.TargetSource;
import com.wyb.test.spring.smallSpring.aop.proxy.ProxyFactory;
import com.wyb.test.spring.smallSpring.ioc.BeanPostProcessor;
import com.wyb.test.spring.smallSpring.ioc.factory.BeanFactory;
import com.wyb.test.spring.smallSpring.ioc.factory.BeanFactoryAware;
import com.wyb.test.spring.smallSpring.ioc.xml.XmlBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AopInfrastructureBean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private static final Logger logger = Logger.getLogger("AspectJAwareAdvisorAutoProxyCreator");

    private XmlBeanFactory xmlBeanFactory;

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) throws Exception {
        /* 这里两个 if 判断很有必要，如果删除将会使程序进入死循环状态，
         * 最终导致 StackOverflowError 错误发生
         */
//        if (bean instanceof AspectJExpressionPointcutAdvisor) {
//            return bean;
//        }
//        if (bean instanceof MethodInterceptor) {
//            return bean;
//        }
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        // 1.  从 BeanFactory 查找 AspectJExpressionPointcutAdvisor 类型的对象
        List<AspectJExpressionPointcutAdvisor> advisors =
                xmlBeanFactory.getBeansForType(AspectJExpressionPointcutAdvisor.class);
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {

            // 2. 使用 Pointcut 对象匹配当前 bean 对象
            if (advisor.getPointcut().getClassFilter().matchers(bean.getClass())) {
                ProxyFactory advisedSupport = new ProxyFactory();
                advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                TargetSource targetSource = new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);

                // 3. 生成代理对象，并返回
                return advisedSupport.getProxy();
            }
        }

        // 2. 匹配失败，返回 bean
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        xmlBeanFactory = (XmlBeanFactory) beanFactory;
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
		boolean retVal = Advice.class.isAssignableFrom(beanClass) ||
				Pointcut.class.isAssignableFrom(beanClass) ||
				Advisor.class.isAssignableFrom(beanClass) ||
				AopInfrastructureBean.class.isAssignableFrom(beanClass);
		if (retVal && !logger.isLoggable(Level.OFF)) {
			logger.info("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
		}
		return retVal;
	}
}
