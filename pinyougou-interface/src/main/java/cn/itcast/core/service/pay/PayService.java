package cn.itcast.core.service.pay;

import java.util.Map;

/**
 * @ClassName PayService
 * @Description 生成二维码接口
 * @Author 传智播客
 * @Date 10:39 2019/5/22
 * @Version 2.1
 **/
public interface PayService {

    /**
     * @author 栗子
     * @Description 生成二维码
     * @Date 10:40 2019/5/22
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> createNative(String username) throws Exception;

    /**
     * @author 栗子
     * @Description 微信查询订单接口
     * @Date 12:10 2019/5/22
     * @param out_trade_no
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> queryPayStatus(String out_trade_no) throws Exception;
}
