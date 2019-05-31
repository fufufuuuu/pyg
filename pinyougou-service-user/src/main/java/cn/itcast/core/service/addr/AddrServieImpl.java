package cn.itcast.core.service.addr;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AddrServieImpl implements AddrService {
    @Resource
    private AddressDao addressDao;

    /**
     * @author 栗子
     * @Description 获取当前账号的收件人的地址列表
     * @Date 12:10 2019/5/21
     * @param userId
     * @return java.util.List<cn.itcast.core.pojo.address.Address>
     **/
    @Override
    public List<Address> findListByLoginUser(String userId) {
        AddressQuery query = new AddressQuery();
        query.createCriteria().andUserIdEqualTo(userId);
        List<Address> list = addressDao.selectByExample(query);
        return list;
    }

    /**
     * 新增地址
     * @param address
     */
    @Override
    public void addAddress(Address address, String name) {
        address.setUserId(name);
        addressDao.insertSelective(address);
    }
}
