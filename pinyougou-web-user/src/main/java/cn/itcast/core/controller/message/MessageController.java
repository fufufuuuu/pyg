package cn.itcast.core.controller.message;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.message.MessageService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * 用户信息设置
 *
 * @author YangZiFan hujunzheng
 * @create 2019-05-29 15:41
 **/
@RestController
@RequestMapping("/message")
public class MessageController {
    @Reference
    private MessageService messageService;
    /**
     * 设置用户的账户信息
     * @param user
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody User user){
        try{
            messageService.update(user);
            return new Result(true,"修改成功！");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"修改失败！");
    }

    /**
     * 查询当前用户的信息
     * @return
     */
    @RequestMapping("/findByOne.do")
    public User findByOne() throws ParseException {
        //从Secruity容器中获取登录用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return messageService.findByOne(name);
    }
} 
