package cn.itcast.core.service.message;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author YangZiFan hujunzheng
 * @create 2019-05-29 16:55
 **/
@Service
public class MessageServiceImpl implements  MessageService {
    @Resource
    private UserDao userDao;

    //定义固定的FastDFS地址
    @Value("192.168.200.128")
    private String FILE_SERVER_URL;
    /**
     * 修改用户的基本信息
     * @param user
     * @return
     */

    @Transactional
    @Override
    public void update(User user) throws ParseException {
       /* //获取生日 年 月 日
        String dateForYear = user.getBirDate().getDateForYear();
        String dateForMonth = user.getBirDate().getDateForMonth();
        String dateForDay = user.getBirDate().getDateForDay();
        //日期转换
        String birthday=dateForYear+"-"+dateForMonth+"-"+dateForDay;
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        //把图片存到FastDFS中
//        picUpLoad(user.getHeadPic());
        //修改用户信息
        userDao.updateByPrimaryKeySelective(user);*/
    }

    /**
     * 查询回显用户逇基本信息
     * @param name
     * @return
     */
    @Override
    public User findByOne(String name) throws ParseException {
        //获取Qurey对象
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(name);
        List<User> users = userDao.selectByExample(userQuery);
        User user = users.get(0);
        Date brTime= user.getBirthday();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = simpleDateFormat.format(brTime);
        if (birthday!=null&&!"".equals(birthday)){
            String[] split = birthday.split("-");
            //获取生日
            Map<String, String> brDate = user.getBrDate();
            String year=split[0];
            String month=split[1];
            String day=split[2];
            brDate.put("year",split[0]);        //获取生日 年
            brDate.put("month",split[1]);       //获取生日 月
            brDate.put("day",split[2]);         //获取生日 日
        }
        return user;
    }

   /* *//**
     * 图片上传到FastDFS的方法
     * @param url
     * @return
     *//*
    private String picUpLoad(String url) throws Exception {
        //获取配置文件的地址
        String conf="classpath:fastDFS/fdfs_client.conf";
        //获取FastDFS的客户端对象
        FastDFSClient fastDFSClient = new FastDFSClient(conf);
        //设置文件的扩展名
        String extName=url.
    }*/
}
