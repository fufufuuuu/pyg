package cn.itcast.core.service.seller;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SellerServiceImpl
 * @Description 商家管理服务
 * @Author 传智播客
 * @Date 12:16 2019/5/5
 * @Version 2.1
 **/
@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDao sellerDao;

    /**
     * @author 栗子
     * @Description 商家入驻申请
     * @Date 12:16 2019/5/5
     * @param seller
     * @return void
     **/
    @Transactional
    @Override
    public void add(Seller seller) {
        // 设置商家的审核状态： 0 待审核
        seller.setStatus("0");
        // 密码加密
        String password = seller.getPassword();
        // 加密：md5、spring盐值、BCrypt（加盐）
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        password = bCryptPasswordEncoder.encode(password);
        seller.setPassword(password);

        sellerDao.insertSelective(seller);
    }

    /**
     * @author 栗子
     * @Description 待审核商家的列表查询
     * @Date 12:32 2019/5/5
     * @param page
     * @param rows
     * @param seller
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page, rows);
        SellerQuery query = new SellerQuery();
        if(seller.getStatus() != null && !"".equals(seller.getStatus())){
            query.createCriteria().andStatusEqualTo(seller.getStatus());
        }
        Page<Seller> p = (Page<Seller>) sellerDao.selectByExample(query);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * @author 栗子
     * @Description 回显商家的详细信息
     * @Date 9:26 2019/5/6
     * @param sellerId
     * @return cn.itcast.core.pojo.seller.Seller
     **/
    @Override
    public Seller findOne(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }

    /**
     * @author 栗子
     * @Description 商家的审核
     * @Date 9:31 2019/5/6
     * @param sellerId
     * @param status
     * @return void
     **/
    @Transactional
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        // 更新
        sellerDao.updateByPrimaryKeySelective(seller);
    }

    /**
     * 更新商家信息
     *
     * @param seller
     * @return
     */
    @Transactional
    @Override
    public void update(Seller seller) {
        sellerDao.updateByPrimaryKey(seller);
    }

    /**
     * 修改保存密码
     *
     * @param SellerId
     */
   /* @Override
    public void updatePassword(String SellerId) {

    }*/


}
