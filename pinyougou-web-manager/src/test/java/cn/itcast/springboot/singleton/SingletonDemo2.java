package cn.itcast.springboot.singleton;

/**
 * @ClassName SingletonDemo2
 * @Description 懒汉式
 * @Author 传智播客
 * @Date 16:12 2019/5/15
 * @Version 2.1
 **/
public class SingletonDemo2 {

    // 私有化构造方法
    private SingletonDemo2(){};

    private static SingletonDemo2 singletonDemo2;

    // 对外提供一个公共的静态的访问方法
    public static SingletonDemo2 getInstance(){

        synchronized (SingletonDemo2.class){
            if(singletonDemo2 == null){
                if(singletonDemo2 == null){
                    singletonDemo2 = new SingletonDemo2();
                }
            }
        }

        return singletonDemo2;
    }
}
