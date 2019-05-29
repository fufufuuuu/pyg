package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName GoodsController
 * @Description 商家的商品管理
 * @Author 传智播客
 * @Date 11:45 2019/5/6
 * @Version 2.1
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * @author 栗子
     * @Description 商品保存
     * @Date 11:46 2019/5/6
     * @param goodsVo
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody GoodsVo goodsVo){
        try {
            // 设置商家的id
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(sellerId);
            goodsService.add(goodsVo);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }

    /**
     * @author 栗子
     * @Description 商家系统的商品列表查询
     * @Date 10:24 2019/5/9
     * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods){
        // 设置商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);
        return goodsService.search(page, rows, goods);
    }

    /**
     * @author 栗子
     * @Description 商品回显
     * @Date 11:10 2019/5/9
     * @param id
     * @return cn.itcast.core.vo.GoodsVo
     **/
    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 更新商品
     * @Date 11:19 2019/5/9
     * @param goodsVo
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/update.do")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {
            goodsService.update(goodsVo);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "修改失败");
    }

}
