package com.wyb.test.spring.smallSpring.ioc;

import com.wyb.test.spring.simpleSpring.ioc.MiniBean;
import com.wyb.test.spring.smallSpring.ioc.xml.XmlBeanFactory;

/**
 * @author Marcher丶
 */
public class XmlBeanFactoryTest {

    public static void main(String[] args) throws Exception {

        System.out.println("--------- IOC test ----------");
        String location = XmlBeanFactoryTest.class.getClassLoader().getResource("smallSpring.xml").getFile();

        XmlBeanFactory bf = new XmlBeanFactory(location);
        MiniBean miniBean = (MiniBean) bf.getBean("miniBean1");
        System.out.println(miniBean.toString());
//        MiniBean miniBean1 = (MiniBean) bf.getBean("miniBean1");
//        System.out.println(miniBean1);
    }
}
