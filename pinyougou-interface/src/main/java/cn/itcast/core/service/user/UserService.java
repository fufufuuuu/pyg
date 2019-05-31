package cn.itcast.core.service.user;

import cn.itcast.core.pojo.user.User;

/**
 * @ClassName UserService
 * @Description 短信发送接口
 * @Author 传智播客
 * @Date 12:35 2019/5/17
 * @Version 2.1
 **/
public interface UserService {

    /**
     * @author 栗子
     * @Description 发送短信
     * @Date 12:37 2019/5/17
     * @param phone
     * @return void
     **/
    void sendCode(String phone);

    /**
     * @author 栗子
     * @Description 用户注册
     * @Date 10:10 2019/5/18
     * @param user
     * @param code 页面输入的验证码
     * @return void
     **/
    void add(User user, String code);


    void updatePassword(String userName,String oldPassword,String newPassword);

    User findByUserName(String userName );
}
