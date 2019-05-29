package cn.itcast.core.service.order;

import cn.itcast.core.pojo.order.Order;

/**
 * @ClassName OrderService
 * @Description 订单接口
 * @Author 传智播客
 * @Date 9:55 2019/5/22
 * @Version 2.1
 **/
public interface OrderService {

    /**
     * @author 栗子
     * @Description 订单提交
     * @Date 9:56 2019/5/22
     * @param username
     * @param order
     * @return void
     **/
    void add(String username, Order order);
}
