package cn.itcast.core.controller.pay;

import cn.itcast.core.entity.Result;
import cn.itcast.core.service.pay.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName PayController
 * @Description 二维码
 * @Author 传智播客
 * @Date 10:44 2019/5/22
 * @Version 2.1
 **/
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    /**
     * @author 栗子
     * @Description 生成二维码
     * @Date 10:45 2019/5/22
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    @RequestMapping("/createNative.do")
    public Map<String, String> createNative() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(username);
    }

    /**
     * @author 栗子
     * @Description 查询订单支付状态
     * @Date 12:16 2019/5/22
     * @param out_trade_no
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/queryPayStatus.do")
    public Result queryPayStatus(String out_trade_no){
        try{
            int i = 0;
            while (true){   // 不能频繁刷，休息一会儿
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                String tradeState = map.get("trade_state");
                if("SUCCESS".equals(tradeState)){ // 支付成功
                    return new Result(true, "支付成功");
                }else{
                    // 等待支付、支付中
                    Thread.sleep(5000);
                    i++;
                }
                if(i > 360){
                    return new Result(false, "二维码超时");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
