package cn.itcast.core.service.cart;

import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;

import java.util.List;

/**
 * @ClassName CartService
 * @Description 购物车接口
 * @Author 传智播客
 * @Date 12:15 2019/5/20
 * @Version 2.1
 **/
public interface CartService {

    /**
     * @author 栗子
     * @Description 获取商家id
     * @Date 12:15 2019/5/20
     * @param id
     * @return cn.itcast.core.pojo.item.Item
     **/
    Item findOne(Long id);

    /**
     * @author 栗子
     * @Description 填充购物车列表页面需要展示的数据
     * @Date 12:42 2019/5/20
     * @param cartList
     * @return java.util.List<cn.itcast.core.pojo.cart.Cart>
     **/
    List<Cart> autoDataToCartList(List<Cart> cartList);

    /**
     * @author 栗子
     * @Description 将购物车保存到redis中
     * @Date 11:09 2019/5/21
     * @param username
     * @param cartList
     * @return void
     **/
    void mergeCartList(String username, List<Cart> cartList);

    /**
     * @author 栗子
     * @Description 从redis中取出购物车
     * @Date 11:28 2019/5/21
     * @param username
     * @return java.util.List<cn.itcast.core.pojo.cart.Cart>
     **/
    List<Cart> findCartListByRedis(String username);
}
