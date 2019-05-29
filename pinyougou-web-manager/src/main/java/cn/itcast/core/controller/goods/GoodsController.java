package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName GoodsController
 * @Description 运营商对商品管理
 * @Author 传智播客
 * @Date 11:39 2019/5/9
 * @Version 2.1
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {


    private Logger logger = LogManager.getLogger(GoodsController.class);

    @Reference
    private GoodsService goodsService;

    /**
     * @author 栗子
     * @Description 运营商查询待审核的商品列表
     * @Date 11:41 2019/5/9
     * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods){
        return goodsService.searchForManager(page, rows, goods);
    }

    /**
     * @author 栗子
     * @Description 审核商品
     * @Date 12:14 2019/5/9
     * @param ids
     * @param status
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status){
        try {
            goodsService.updateStatus(ids, status);
//            System.out.println(9/0);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("商品审核的程序出错了：" + e.getMessage());
        }
        return new Result(false, "操作失败");
    }

    /**
     * @author 栗子
     * @Description 删除商品
     * @Date 12:23 2019/5/9
     * @param ids
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            goodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }

}
