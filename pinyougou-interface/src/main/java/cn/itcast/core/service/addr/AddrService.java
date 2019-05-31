package cn.itcast.core.service.addr;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

/**
 * @ClassName AddrService
 * @Description 当前收货人的地址接口
 * @Author 传智播客
 * @Date 12:06 2019/5/21
 * @Version 2.1
 **/
public interface AddrService {

    /**
     * @author 栗子
     * @Description 获取当前账号的收件人的地址列表
     * @Date 12:09 2019/5/21
      * @param userId
     * @return java.util.List<cn.itcast.core.pojo.address.Address>
     **/
    List<Address> findListByLoginUser(String userId);

    void addAddress(Address address, String name);
}
