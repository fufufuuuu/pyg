package cn.itcast.core.controller.order;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrderController
 * @Description 订单操作
 * @Author 传智播客
 * @Date 10:17 2019/5/22
 * @Version 2.1
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     * @author 栗子
     * @Description 提交订单
     * @Date 10:19 2019/5/22
     * @param order
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody Order order){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.add(username, order);
            return new Result(true, "订单提交成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "订单提交失败");
    }
}
