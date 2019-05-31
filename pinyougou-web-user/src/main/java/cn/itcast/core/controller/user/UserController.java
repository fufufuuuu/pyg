package cn.itcast.core.controller.user;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.checkphone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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

    @RequestMapping("/updatePassword.do")
    public Result updatePassword(String OldPassword,String password){
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updatePassword(userName,OldPassword,password);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"修改失败");

    }

    @RequestMapping("/verifyImage.do")
    public void verifyImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //  创建缓存图片：指定宽width=90，高 height=30
        int width = 90;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //  获取画笔对象
        Graphics g = image.getGraphics();
        //  设置画笔颜色
        // 设置画笔颜色：白色
        g.setColor(Color.white);
        // 填充矩形区域
        g.fillRect(0, 0, width, height);

        //  从字符数组中随机得到字符
        char[] arr = { 'A', 'B', 'C', 'D', 'N', 'E', 'W', 'b', 'o', 'y', '1', '2', '3', '4' };
        // 创建随机对象
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            // 随机获得一个索引
            int randomIndex = r.nextInt(arr.length);
            // 根据索引获得字符：要绘制到图片上的
            char ch = arr[randomIndex];

            // 设置字体，大小为 18
            g.setFont(new Font(Font.DIALOG,Font.BOLD + Font.ITALIC,18));
            // 设置字的颜色随机
            g.setColor(randomColor());

            //  将每个字符画到图片上，位置：5+(i*20), 20
            g.drawString(String.valueOf(ch), (i * 20) + 5 , 20);
        }
        //  画10条干扰线，线的位置是随机的，x 范围在 width 之中，y 的范围在 height 之中。
        for (int i = 0; i < 10; i++) {
            // 起点
            int x1 = r.nextInt(width);
            int y1 = r.nextInt(height);

            // 终点
            int x2 = r.nextInt(width);
            int y2 = r.nextInt(height);

            // 设置字的颜色随机
            g.setColor(randomColor());

            g.drawLine(x1, y1, x2,y2 );

        }
        //  将缓存的图片输出到响应输出流中
        ImageIO.write(image,"png" , response.getOutputStream());
    }
    //  写一个方法随机获取颜色
    public Color randomColor(){
        // 三原色：red，green，blue  取值：0到255
        Random r = new Random();
        return  new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256));
    }
}
