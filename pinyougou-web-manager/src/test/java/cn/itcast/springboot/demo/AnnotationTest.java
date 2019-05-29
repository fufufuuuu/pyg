package cn.itcast.springboot.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName AnnotationTest
 * @Description 测试注解
 * @Author 传智播客
 * @Date 9:20 2019/5/9
 * @Version 2.1
 **/
public class AnnotationTest {

    public static void main(String[] args) {
        // 创建spring容器
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        // @Configuration:相当于spring.xml <beans></beans>   spring容器。
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfiguration.class);
        // 获取myBean
        MyBean myBean = (MyBean) applicationContext.getBean("xxx");
        myBean.sayHello();
        // 获取myBean2
        MyBean2 myBean2 = (MyBean2) applicationContext.getBean("myBean2");
        myBean2.sayHelloTo();

    }
}
