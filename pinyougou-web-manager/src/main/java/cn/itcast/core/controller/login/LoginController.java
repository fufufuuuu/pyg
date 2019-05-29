package cn.itcast.core.controller.login;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description 显示当前的登录人
 * @Author 传智播客
 * @Date 11:58 2019/5/5
 * @Version 2.1
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/showName.do")
    public Map<String, String> showName(){
        Map<String, String> map = new HashMap<>();
        // 登录人：在springsecurity容器中
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username", username);
        return map;
    }
}
