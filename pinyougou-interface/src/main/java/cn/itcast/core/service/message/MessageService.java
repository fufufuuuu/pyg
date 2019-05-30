package cn.itcast.core.service.message;

import cn.itcast.core.pojo.user.User;

import java.text.ParseException;

public interface MessageService {
    /**
     * 修改用户的基本信息
     * @param user
     * @return
     */
    void update(User user) throws ParseException;

    /**
     * 查询回显用户逇基本信息
     * @return
     */
    User findByOne(String name) throws ParseException;
}
