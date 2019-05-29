package cn.itcast.core.controller.cart;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CartController
 * @Description 商品加入购物车
 * @Author 传智播客
 * @Date 10:22 2019/5/20
 * @Version 2.1
 **/
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    // 解决服务器端的跨域请求
//            response.setHeader("Access-Control-Allow-Origin", "http://localhost:9003");
//            response.setHeader("Access-Control-Allow-Credentials", "true"); // 支持携带的cookie信息

    /**
     * @author 栗子
     * @Description 将商品加入购物车
     * @Date 10:24 2019/5/20
      * @param itemId
     * @param num
     * @return cn.itcast.core.entity.Result
     **/
//    @CrossOrigin(origins = {"http://localhost:9003"}, allowCredentials = "true")
    @CrossOrigin(origins = {"http://localhost:9003"})
    @RequestMapping("/addGoodsToCartList.do")
    public Result addGoodsToCartList(Long itemId, Integer num,
                                     HttpServletRequest request, HttpServletResponse response){
        try{
            // 将商品加入购物车的具体的业务实现
            // 1、定一个空车的集合对象
            List<Cart> cartList = null;
            // 定义开关（标记）
            boolean flag = false;
            // 2、判断本地(cookie)是否有购物车
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0){
                for (Cookie cookie : cookies) {
                    if("BUYER_CART".equals(cookie.getName())){
                        // 3、有：直接取出并且赋值
                        // cookie：key-value（String）
                        String text = cookie.getValue(); // 数据：List<Cart>----json
                        cartList = JSON.parseArray(text, Cart.class);
                        flag = true;    // 打开开关
                        // 找到跳出循环
                        break;
                    }
                }
            }
            // 4、么有：说明是第一次，创建一个车子
            if(cartList == null){
                cartList = new ArrayList<>();
            }
            // 有车啦
            // 将数据先封装到cart中
            Cart cart = new Cart();
            Item item = cartService.findOne(itemId);
            cart.setSellerId(item.getSellerId());   // 商家id
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(itemId);    // 对cookie瘦身
            orderItem.setNum(num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            // 5、将商品(cart)装车
            // 5.1、判断该商品是否属于同一个商家(商家的id一样：同一个商家)
            int sellerIndexOf = cartList.indexOf(cart);
            if(sellerIndexOf != -1){
                // 是同一个商家：继续判断是否是同款商品
                // 获取当前商家下的购物项
                List<OrderItem> oldOrderItemList = cartList.get(sellerIndexOf).getOrderItemList();
                int itemIndexOf = oldOrderItemList.indexOf(orderItem);  // 是否是同款商品：判断skuId是否一样
                if(itemIndexOf != -1){
                    // 同款商品:合并数量
                    OrderItem oldOrderItem = oldOrderItemList.get(itemIndexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum() + num);
                }else{
                    // 同商家不同商品：添加到该商家的购物项集中
                    oldOrderItemList.add(orderItem);
                }
            }else{
                // 5.2、不是同一个商家：直接装车
                cartList.add(cart);
            }
            // 判断用户是否登录
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//            System.out.println("username:" + username);
            if(!"anonymousUser".equals(username)){ // 已登录
                // 6-1、将购物车保存到服务器端（redis）
                cartService.mergeCartList(username, cartList);
                // 清空本地的购物车
                if(flag){
                    Cookie cookie = new Cookie("BUYER_CART", null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");    // 设置cookie共享
                    response.addCookie(cookie);
                }

            }else{ // 未登录
                // 6-2、将购物车保存到本地（cookie）
                Cookie cookie = new Cookie("BUYER_CART", JSON.toJSONString(cartList));
                cookie.setMaxAge(60*60);
                // ip1:port1/   ip2:port2/
                cookie.setPath("/");    // 设置cookie共享
                response.addCookie(cookie);
            }

            return new Result(true, "添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false, "添加失败");
    }

    /**
     * @author 栗子
     * @Description 回显购物车列表数据
     * @Date 12:58 2019/5/20
     * @param request
     * @return java.util.List<cn.itcast.core.pojo.cart.Cart>
     **/
    @RequestMapping("/findCartList.do")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response){
        // 判断用户是否登录
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 1、未登录：从本地（cookie）中获取
        List<Cart> cartList = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if("BUYER_CART".equals(cookie.getName())){
                    String text = cookie.getValue(); // 数据：List<Cart>----json
                    cartList = JSON.parseArray(text, Cart.class);
                    break;
                }
            }
        }
        // 2、已登录
        if(!"anonymousUser".equals(username)){
            // 将本地的cookie的购物车的数据同步到redis中
            if(cartList != null){
                cartService.mergeCartList(username, cartList);
                // 清空本地
                Cookie cookie = new Cookie("BUYER_CART", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");    // 设置cookie共享
                response.addCookie(cookie);
            }
            // 从redis中取出购物车
            cartList = cartService.findCartListByRedis(username);
        }
        // 填充购物车的列表数据
        if(cartList != null){
            // 填充购物车列表页面需要展示的数据
            cartList = cartService.autoDataToCartList(cartList);
        }


        return cartList;

    }
}
