package cn.itcast.core.service.cart;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CartServiceImpl
 * @Description 购物车服务
 * @Author 传智播客
 * @Date 12:16 2019/5/20
 * @Version 2.1
 **/
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ItemDao itemDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @author 栗子
     * @Description 获取商家id
     * @Date 12:16 2019/5/20
      * @param id
     * @return cn.itcast.core.pojo.item.Item
     **/
    @Override
    public Item findOne(Long id) {
        return itemDao.selectByPrimaryKey(id);
    }

    /**
     * @author 栗子
     * @Description 填充购物车列表页面需要展示的数据
     * @Date 12:42 2019/5/20
     * @param cartList
     * @return java.util.List<cn.itcast.core.pojo.cart.Cart>
     **/
    @Override
    public List<Cart> autoDataToCartList(List<Cart> cartList) {
        for (Cart cart : cartList) {
            Seller seller = sellerDao.selectByPrimaryKey(cart.getSellerId());
            cart.setSellerName(seller.getNickName());   // 商家店铺名称
            // 填充购物项的数据
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                orderItem.setPicPath(item.getImage());      // 商品图片
                orderItem.setTitle(item.getTitle());        // 商品标题
                orderItem.setPrice(item.getPrice());        // 商品单价
                BigDecimal totalFee = new BigDecimal(item.getPrice().doubleValue() * orderItem.getNum());
                orderItem.setTotalFee(totalFee);            // 商品小计 = 单价 * 数量
            }
        }
        return cartList;
    }

    /**
     * @author 栗子
     * @Description 将购物车保存到redis中
     * @Date 11:09 2019/5/21
     * @param username
     * @param newCartList
     * @return void
     **/
    @Override
    public void mergeCartList(String username, List<Cart> newCartList) {
        // 取出老车
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        // 将新车合并到老车中
        oldCartList = mergeNewCartListToOldCartList(newCartList, oldCartList);
        // 保存老车
        redisTemplate.boundHashOps("BUYER_CART").put(username, oldCartList);
    }

    /**
     * @author 栗子
     * @Description 从redis中取出购物车
     * @Date 11:28 2019/5/21
     * @param username
     * @return java.util.List<cn.itcast.core.pojo.cart.Cart>
     **/
    @Override
    public List<Cart> findCartListByRedis(String username) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        return cartList;
    }

    // 将新车的数据合并到老车中
    private List<Cart> mergeNewCartListToOldCartList(List<Cart> newCartList, List<Cart> oldCartList) {
        if(newCartList != null){
           if(oldCartList != null){
               // 新车、老车都不为null，开始合并：商品装车
               for (Cart newCart : newCartList) {
                   // 判断该商品是否属于同一个商家
                   int sellerIndexOf = oldCartList.indexOf(newCart);
                   if(sellerIndexOf != -1){
                       // 同一个商家
                       // 继续判断：是否属于同款商品
                       List<OrderItem> newOrderItemList = newCart.getOrderItemList();
                       List<OrderItem> oldOrderItemList = oldCartList.get(sellerIndexOf).getOrderItemList();
                       for (OrderItem newOrderItem : newOrderItemList) {
                           int itemIndexOf = oldOrderItemList.indexOf(newOrderItem);
                           if(itemIndexOf != -1){
                               // 同款商品：合并购买数量
                               OrderItem oldOrderItem = oldOrderItemList.get(itemIndexOf);
                               oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                           }else{
                               // 同商家不同商品
                               oldOrderItemList.add(newOrderItem);
                           }
                       }
                   }else{
                       // 不是同一个商家，直接装车
                       oldCartList.add(newCart);
                   }
               }
           } else {
               return newCartList;
           }
        } else {
            // 新车为null，直接返回老车
            return oldCartList;
        }
        return oldCartList;
    }
}
