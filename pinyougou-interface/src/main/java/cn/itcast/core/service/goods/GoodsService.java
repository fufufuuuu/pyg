package cn.itcast.core.service.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

/**
 * @ClassName GoodsService
 * @Description 商品接口
 * @Author 传智播客
 * @Date 11:36 2019/5/6
 * @Version 2.1
 **/
public interface GoodsService {

    /**
     * @author 栗子
     * @Description 保存商品
     * @Date 11:37 2019/5/6
     * @param goodsVo
     * @return void
     **/
    void add(GoodsVo goodsVo);

    /**
     * @author 栗子
     * @Description 商家系统的商品列表查询
     * @Date 10:16 2019/5/9
     * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult search(Integer page, Integer rows, Goods goods);

    /**
     * @author 栗子
     * @Description 回显商品
     * @Date 11:07 2019/5/9
     * @param id
     * @return cn.itcast.core.vo.GoodsVo
     **/
    GoodsVo findOne(Long id);

    /**
     * @author 栗子
     * @Description 商品更新
     * @Date 11:11 2019/5/9
     * @param goodsVo
     * @return void
     **/
    void update(GoodsVo goodsVo);

    /**
     * @author 栗子
     * @Description 运营系统的商品列表查询
     * @Date 11:35 2019/5/9
      * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult searchForManager(Integer page, Integer rows, Goods goods);

    /**
     * @author 栗子
     * @Description 商品审核
     * @Date 12:08 2019/5/9
     * @param ids
     * @param status
     * @return void
     **/
    void updateStatus(Long[] ids, String status);


    /**
     * @author 栗子
     * @Description 删除商品
     * @Date 12:21 2019/5/9
     * @param ids
     * @return void
     **/
    void delete(Long[] ids);
}
