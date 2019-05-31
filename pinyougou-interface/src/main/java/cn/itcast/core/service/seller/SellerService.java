package cn.itcast.core.service.seller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;

import java.util.List;

/**
 * @ClassName SellerService
 * @Description 商家管理接口
 * @Author 传智播客
 * @Date 12:15 2019/5/5
 * @Version 2.1
 **/
public interface SellerService {

    /**
     * @author 栗子
     * @Description 商家入驻申请
     * @Date 12:15 2019/5/5
     * @param seller
     * @return void
     **/
    void add(Seller seller);

    /**
     * @author 栗子
     * @Description 待审核商家的列表查询
     * @Date 12:32 2019/5/5
     * @param page
     * @param rows
     * @param seller
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult search(Integer page, Integer rows, Seller seller);

    /**
     * @author 栗子
     * @Description 回显商家的详细信息
     * @Date 9:25 2019/5/6
     * @param sellerId
     * @return cn.itcast.core.pojo.seller.Seller
     **/
    Seller findOne(String sellerId);

    /**
     * @author 栗子
     * @Description 商家的审核
     * @Date 9:30 2019/5/6
     * @param sellerId
     * @param status 商家的审核状态 0 待审核  1 审核通过  2 审核未通过 3 关闭
     * @return void
     **/
    void updateStatus(String sellerId, String status);


    /**
     * 更新商家信息
     * @param seller
     * @return
     */
   void update(Seller seller);

    /**
     * 修改保存密码
     */
   /* void updatePassword(String SellerId);*/



}

