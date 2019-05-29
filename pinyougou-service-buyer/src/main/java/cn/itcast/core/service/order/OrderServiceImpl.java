package cn.itcast.core.service.order;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.utils.uniqueuekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderServiceImpl
 * @Description 订单服务
 * @Author 传智播客
 * @Date 9:57 2019/5/22
 * @Version 2.1
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IdWorker idWorker;

    @Resource
    private ItemDao itemDao;

    @Resource
    private PayLogDao payLogDao;

    /**
     * @author 栗子
     * @Description 提交订单
     * @Date 9:58 2019/5/22
     * @param username
     * @param order
     * @return void
     **/
    @Transactional
    @Override
    public void add(String username, Order order) {
        // 1、保存订单---已商家为单位
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        if(cartList != null && cartList.size() > 0){
            double fee = 0f;    // 整个订单的总金额
            List<Long> orderList = new ArrayList<>();   // 订单号的列表
            for (Cart cart : cartList) {
                long orderId = idWorker.nextId();
                orderList.add(orderId);
                order.setOrderId(orderId);  // idWorker生成
                double payment = 0f;        // 该商家下的订单总金额 = 订单明细的总价
                order.setPaymentType("1");  // 支付类型：在线支付
                order.setStatus("1");       // 订单状态：待付款
                order.setCreateTime(new Date());    // 订单提交的日期
                order.setUserId(username);  // 下单的用户
                order.setSourceType("1");   // 订单来源：pc端
                order.setSellerId(cart.getSellerId());  // 商家id

                // 2、保存订单明细
                List<OrderItem> orderItemList = cart.getOrderItemList();
                if(orderItemList != null && orderItemList.size() > 0){
                    for (OrderItem orderItem : orderItemList) {
                        long id = idWorker.nextId();
                        orderItem.setId(id);
                        Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        orderItem.setGoodsId(item.getGoodsId());    // spu的id
                        orderItem.setOrderId(orderId);              // 订单id
                        orderItem.setTitle(item.getTitle());        // 商品标题
                        orderItem.setPrice(item.getPrice());        // 商品单价
                        double totalFee = item.getPrice().doubleValue() * orderItem.getNum();
                        payment += totalFee;
                        orderItem.setTotalFee(new BigDecimal(totalFee));            // 该商品的总金额 = 单价 * 数量
                        orderItem.setPicPath(item.getImage());      // 商品图片
                        orderItem.setSellerId(item.getSeller());    // 店铺名称
                        // 保存明细
                        orderItemDao.insertSelective(orderItem);
                    }
                }
                fee += payment;
                order.setPayment(new BigDecimal(payment));  // 该商家的订单总金额
                // 保存订单
                orderDao.insertSelective(order);
            }

            // 创建交易日志
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));    // 交易日志（流水号）
            payLog.setCreateTime(new Date());   // 日志创建日期
            payLog.setTotalFee((long)fee*100);       // 订单总金额：分
            payLog.setUserId(username);         // 用户的订单
            payLog.setTradeState("0");          // 订单支付状态：待支付
            payLog.setOrderList(orderList.toString().replace("[", "").replace("]",""));     // 订单列表 [32132398989,984923277]
            payLog.setPayType("1");             // 在线支付
            payLogDao.insertSelective(payLog);
            // 将交易日志放到redis中---支付过程中需要改数据
            redisTemplate.boundHashOps("paylog").put(username, payLog);
        }

        // 3、删除购物车
        redisTemplate.boundHashOps("BUYER_CART").delete(username);
    }
}
