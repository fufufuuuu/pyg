package cn.itcast.core.controller.seller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.seller.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SellerController
 * @Description 商家入驻管理
 * @Author 传智播客
 * @Date 12:20 2019/5/5
 * @Version 2.1
 **/
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * @author 栗子
     * @Description 商家入驻申请
     * @Date 12:21 2019/5/5
     * @param seller
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody Seller seller){
        try {
            sellerService.add(seller);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "注册失败");
    }
    @RequestMapping("/findOne.do")
    public Seller findOne() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sellerService.findOne(username);
    }

    /**
     *  更新商家的信息
     * @param seller
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Seller seller) {
        try {
            sellerService.update(seller);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "更新失败");
    }
  /*  @RequestMapping("/updatePassword.do")
    public Result updatePassword(String oldPassword,String newPassword,String newPasswordOne) {

    }*/
}
