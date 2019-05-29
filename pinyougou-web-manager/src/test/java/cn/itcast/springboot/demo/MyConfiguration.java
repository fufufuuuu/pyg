package cn.itcast.springboot.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MyConfiguration
 * @Description 测试Configuration注解
 * @Author 传智播客
 * @Date 9:18 2019/5/9
 * @Version 2.1
 **/
@Configuration
@ComponentScan(basePackages = {"cn.itcast.springboot.demo"})
public class MyConfiguration {

    public MyConfiguration() {
        System.out.println("MyConfiguration 被实例化了");
    }

    // 注入bean
    @Bean(name = "xxx", initMethod = "init")
    public MyBean myBean(){
        return new MyBean();
    }
}
