package cn.itcast.core.controller.user;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.checkphone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description 短信发送
 * @Author 传智播客
 * @Date 12:44 2019/5/17
 * @Version 2.1
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/sendCode.do")
    public Result sendCode(String phone){
        try {
            // 对手机号进行校验
            boolean phoneLegal = PhoneFormatCheckUtils.isPhoneLegal(phone);
            if(!phoneLegal){
                return new Result(false, "手机号不合法");
            }
            userService.sendCode(phone);
            return new Result(true, "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, "发送成功");
    }

    /**
     * @author 栗子
     * @Description 用户注册
     * @Date 10:21 2019/5/18
     * @param smscode
     * @param user
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(String smscode, @RequestBody User user){
        try {
            userService.add(user, smscode);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "注册失败");
    }
}
