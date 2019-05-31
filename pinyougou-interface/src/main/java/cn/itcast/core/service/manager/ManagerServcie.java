package cn.itcast.core.service.manager;


import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface ManagerServcie {
    /**
     * 添加
     * @param userName
     * @param address
     */
    void add(String userName,Address address);

    /**
     * 修改
     * @param userName
     * @param address
     */
    void update(String userName,Address address);

    /**
     * 删除
     * @param id
     */
   void delete(Long id);

    /**
     * 回显
     * @param userName
     * @return
     */
    List<Address> findByOne(String userName);
}
