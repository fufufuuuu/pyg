package cn.itcast.core.service.itemcat;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

/**
 * @ClassName ItemCatService
 * @Description 商品分类接口
 * @Author 传智播客
 * @Date 10:14 2019/5/6
 * @Version 2.1
 **/
public interface ItemCatService {

    /**
     * @author 栗子
     * @Description 商品分类的列表查询
     * @Date 10:15 2019/5/6
     * @param parentId
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    List<ItemCat> findByParentId(Long parentId);

    /**
     * @author 栗子
     * @Description 通过三级分类获取模板id
     * @Date 11:18 2019/5/8
     * @param id
     * @return cn.itcast.core.pojo.item.ItemCat
     **/
    ItemCat findOne(Long id);

    /**
     * @author 栗子
     * @Description 查询所有的分类列表
     * @Date 10:41 2019/5/9
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    List<ItemCat> findAll();

    /**
     * @author 栗子
     * @Description 保存分类
     * @Date 16:50 2019/5/22
      * @param itemCat
     * @return void
     **/
    void add(ItemCat itemCat);
}
