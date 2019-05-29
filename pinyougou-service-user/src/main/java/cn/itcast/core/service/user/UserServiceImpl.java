package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.utils.md5.MD5Util;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServiceImpl
 * @Description 短信发送实现
 * @Author 传智播客
 * @Date 12:37 2019/5/17
 * @Version 2.1
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private Destination smsDestination;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserDao userDao;

    /**
     * @author 栗子
     * @Description 发送短信需要的参数-发送到mq中
     * @Date 12:38 2019/5/17
     * @param phone
     * @return void
     **/
    @Override
    public void sendCode(final String phone) {
        // 6位验证码
        final String code = RandomStringUtils.randomNumeric(6);
        // 保存验证码并且设置过期时间
        redisTemplate.boundValueOps(phone).set(code);
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);
        System.out.println("code"+code);
        // 发送数据到mq
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // map消息体
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("signName", "阮文");
                mapMessage.setString("templateCode", "SMS_140720901");
                mapMessage.setString("templateParam", "{\"code\":\""+code+"\"}");
                return mapMessage;
            }
        });
    }
    /**
     * @author 栗子
     * @Description 用户注册
     * @Date 10:11 2019/5/18
     * @param user
     * @param code
     * @return void
     **/
    @Override
    public void add(User user, String code) {
        // 密码加密：MD5 --- CAS
        // 判断验证码是否正确
        String phoneCode = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(code != null && !"".equals(code) && phoneCode != null && code.equals(phoneCode)){
            // 验证码正确的
            String password = user.getPassword();
            password = MD5Util.MD5Encode(password, null);
            user.setPassword(password); // 密码加密md
            user.setCreated(new Date());
            user.setUpdated(new Date());
            // 用户保存
            userDao.insertSelective(user);
        }else{
            throw new RuntimeException("您输入的验证码不正确");
        }
    }
}
