package cn.itcast.core.service.manager;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地址管理
 *
 * @author YangZiFan hujunzheng
 * @create 2019-05-30 9:34
 **/
@Service
public class ManagerServiceImpl implements ManagerServcie {
    @Resource
    private AddressDao addressDao;

    /**
     * 添加
     * @param userName
     * @param address
     */
    @Transactional
    @Override
    public void add(String userName,Address address) {
        //封装用户名参数
        address.setUserId(userName);
        //添加数据到数据库
        addressDao.insertSelective(address);
    }

    /**
     * 修改
     * @param userName
     * @param address
     */
    @Transactional
    @Override
    public void update(String userName,Address address) {
        //获取Query对象
        AddressQuery addressQuery = new AddressQuery();
        //封装条件
        addressQuery.createCriteria().andIdEqualTo(address.getId()).andUserIdEqualTo(userName);
        //修改
        addressDao.updateByExample(address,addressQuery);
    }

    /**
     * 删除
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        addressDao.deleteByPrimaryKey(id);
    }

    /**
     * 回显
     * @param userName
     * @return
     */
    @Override
    public List<Address> findByOne(String  userName) {
        //获取Query对象
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(userName);
        //用户地址列表
        List<Address> addressList = addressDao.selectByExample(addressQuery);
        return addressList;
    }
}
