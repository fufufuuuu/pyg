package cn.itcast.core.controller.seller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.seller.SellerService;
import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SellerController
 * @Description 运营商对商家的管理
 * @Author 传智播客
 * @Date 12:34 2019/5/5
 * @Version 2.1
 **/
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * @author 栗子
     * @Description 待审核商家的列表查询
     * @Date 12:36 2019/5/5
     * @param page
     * @param rows
     * @param seller
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Seller seller){
        return sellerService.search(page, rows, seller);
    }

    /**
     * @author 栗子
     * @Description 回显商家的详细信息
     * @Date 9:28 2019/5/6
     * @param id
     * @return cn.itcast.core.pojo.seller.Seller
     **/
    @RequestMapping("/findOne.do")
    public Seller findOne(String id){
        return sellerService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 商家的审核
     * @Date 9:35 2019/5/6
     * @param sellerId
     * @param status
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(String sellerId, String status){
        try {
            sellerService.updateStatus(sellerId, status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "操作失败");
    }
}
