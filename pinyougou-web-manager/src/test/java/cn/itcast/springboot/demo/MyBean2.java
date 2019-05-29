package cn.itcast.springboot.demo;

import org.springframework.stereotype.Component;

/**
 * @ClassName MyBean2
 * @Description ixxx
 * @Author 传智播客
 * @Date 9:28 2019/5/9
 * @Version 2.1
 **/
@Component
public class MyBean2 {

    public MyBean2() {
        System.out.println("myBean2 构造");
    }

    public void sayHelloTo(){
        System.out.println("myBean2 say hello");
    }
}
