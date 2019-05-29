package cn.itcast.springboot.demo;

/**
 * @ClassName MyBean
 * @Description 定义的bean
 * @Author 传智播客
 * @Date 9:23 2019/5/9
 * @Version 2.1
 **/
public class MyBean {

    public MyBean() {
        System.out.println("myBean 构造方法");
    }

    public void sayHello(){
        System.out.println("myBean say hello");
    }

    public void init(){
        System.out.println("myBean执行init方法。。。");
    }
}
