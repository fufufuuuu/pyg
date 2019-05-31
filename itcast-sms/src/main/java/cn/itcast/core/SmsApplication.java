package cn.itcast.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName SmsApplication
 * @Description 短信平台的引导程序
 * @Author 传智播客
 * @Date 12:19 2019/5/17
 * @Version 2.1
 **/
@SpringBootApplication
public class SmsApplication {

    public static void main(String[] args) {
        // run一下，发布该应用
        SpringApplication.run(SmsApplication.class, args);
    }
}
