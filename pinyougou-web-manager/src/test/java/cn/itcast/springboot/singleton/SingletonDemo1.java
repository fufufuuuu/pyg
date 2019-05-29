package cn.itcast.springboot.singleton;

/**
 * @ClassName SingletonDemo1
 * @Description 饿汉式
 * @Author 传智播客
 * @Date 16:09 2019/5/15
 * @Version 2.1
 **/
public class SingletonDemo1 {

    // 私有化构造方法
    private SingletonDemo1(){};

    private static SingletonDemo1 singletonDemo1 = new SingletonDemo1();

    // 对外提供一个公共的静态的访问方法
    public static SingletonDemo1 getInstance(){
        return singletonDemo1;
    }
}
