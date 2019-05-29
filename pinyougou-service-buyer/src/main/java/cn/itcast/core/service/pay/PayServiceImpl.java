package cn.itcast.core.service.pay;

import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.utils.http.HttpClient;
import cn.itcast.core.utils.uniqueuekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import org.opensaml.xml.signature.Y;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PayServiceImpl
 * @Description 生成二维码的实现
 * @Author 传智播客
 * @Date 10:41 2019/5/22
 * @Version 2.1
 **/
@Service
public class PayServiceImpl implements PayService {

    @Resource
    private IdWorker idWorker;

    @Value("${appid}")
    private String appid;       // 微信公众账号或开放平台APP的唯一标识

    @Value("${partner}")
    private String partner;     // 财付通平台的商户账号

    @Value("${partnerkey}")
    private String partnerkey;  // 财付通平台的商户密钥

    @Value("${notifyurl}")
    private String notifyurl;   // 回调地址

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PayLogDao payLogDao;

    /**
     * @author 栗子
     * @Description 生成二维码
     * @Date 10:41 2019/5/22
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    @Override
    public Map<String, String> createNative(String username) throws Exception {

        // 获取paylog
        PayLog payLog = (PayLog) redisTemplate.boundHashOps("paylog").get(username);

        // 调用微信统一下单接口
        // 1、接口地址
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        // 阅读接口文档：请求与响应的数据格式：xml
//        String xml = "<xml>...</xml>"; 自己拼接xml。使用微信工具的类map--->xml
        // 2、封装接口需要的参数
        Map<String, String> data = new HashMap<>();
//        long out_trade_no = idWorker.nextId();
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
        data.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
        data.put("mch_id", partner);
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
        data.put("nonce_str", WXPayUtil.generateNonceStr());
//        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
//        商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
        data.put("body", "品优购订单支付");
//        商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
        data.put("out_trade_no", String.valueOf(payLog.getOutTradeNo()));
//        标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
//       上线后代码
//        data.put("total_fee", String.valueOf(payLog.getTotalFee()));
        data.put("total_fee","1");    // 支付金额
//        终端IP	spbill_create_ip	是	String(64)	123.12.12.123	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
        data.put("spbill_create_ip", "123.12.12.123");
//        通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        data.put("notify_url", notifyurl);
//        交易类型	trade_type	是	String(16)	JSAPI
        data.put("trade_type", "NATIVE");
        // 将map转成xml
        String xmlParam = WXPayUtil.generateSignedXml(data, partnerkey);
        // 3、模拟浏览器发送请求（HttpClient）
        HttpClient httpClient = new HttpClient(url);
        httpClient.setXmlParam(xmlParam);
        httpClient.isHttps();   // https
        httpClient.post();      // post提交


        // 4、请求完成后响应结果
        String strXML = httpClient.getContent();   // xml
        Map<String, String> map = WXPayUtil.xmlToMap(strXML);
        map.put("total_fee", String.valueOf(payLog.getTotalFee()));    // 展示的金额
//        map.put("out_trade_no", String.valueOf(out_trade_no));  // 订单号
        map.put("out_trade_no", payLog.getOutTradeNo());    // 交易订单号
//        map.put("code_url", "www.itcast.cn");
        return map;
    }

    /**
     * @author 栗子
     * @Description 微信查询订单接口
     * @Date 12:10 2019/5/22
     * @param out_trade_no
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) throws Exception {
        // 1、查询订单的接口地址
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        // 2、将接口需要的数据封装到map中
        Map<String, String> data = new HashMap<>();
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
        data.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
        data.put("mch_id", partner);
//        微信订单号	transaction_id	二选一	String(32)	1009660380201506130728806387	微信的订单号，建议优先使用
//        商户订单号	out_trade_no	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
        data.put("out_trade_no", out_trade_no);
//        随机字符串	nonce_str	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	随机字符串，不长于32位。推荐随机数生成算法
        data.put("nonce_str", WXPayUtil.generateNonceStr());
//        签名	sign	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	通过签名算法计算得出的签名值，详见签名生成算法
        // 3、将map的数据转成接口需要的数据类型：xml
        String xmlParam = WXPayUtil.generateSignedXml(data, partnerkey);
        // 4、通过httpclient发送请求
        HttpClient httpClient = new HttpClient(url);
        httpClient.setXmlParam(xmlParam);
        httpClient.isHttps();   // https
        httpClient.post();      // post提交
        // 5、响应返回结果：xml--->map
        String strXML = httpClient.getContent();
        Map<String, String> map = WXPayUtil.xmlToMap(strXML);
        // 6、交易成功，更新支付日志
        String tradeState = map.get("trade_state");
        if("SUCCESS".equals(tradeState)){   // 支付成功
            // 更新日志
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(out_trade_no); // 主键
            payLog.setPayTime(new Date());      // 支付完成时间
            payLog.setTransactionId(map.get("transaction_id"));          // 第三方提供的交易号码
            payLog.setTradeState("1");          // 支付成功
            payLogDao.updateByPrimaryKeySelective(payLog);
            // 删除redis日志
//            redisTemplate.boundHashOps("paylog").delete(username);
        }
        return map;
    }
}
