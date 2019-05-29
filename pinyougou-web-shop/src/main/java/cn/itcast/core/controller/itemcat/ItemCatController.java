package cn.itcast.core.controller.itemcat;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ItemCatController
 * @Description 商品的分类管理
 * @Author 传智播客
 * @Date 10:18 2019/5/6
 * @Version 2.1
 **/
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * @author 栗子
     * @Description 分类的列表查询
     * @Date 10:19 2019/5/6
     * @param parentId
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @RequestMapping("/findByParentId.do")
    public List<ItemCat> findByParentId(Long parentId){
        return itemCatService.findByParentId(parentId);
    }

    /**
     * @author 栗子
     * @Description 通过三级分类获取模板id
     * @Date 11:20 2019/5/8
     * @param id
     * @return cn.itcast.core.pojo.item.ItemCat
     **/
    @RequestMapping("/findOne.do")
    public ItemCat findOne(Long id){
        return itemCatService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 显示商品列表的分类名称
     * @Date 10:42 2019/5/9
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @RequestMapping("/findAll.do")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }
}
