package cn.itcast.core.controller.itemcat;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @Description 回显商品列表的分类名称
     * @Date 11:44 2019/5/9
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @RequestMapping("/findAll.do")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }

    /**
     * @author 栗子
     * @Description 保存分类
     * @Date 16:50 2019/5/22
     * @param itemCat
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody ItemCat itemCat){
        try {
            itemCatService.add(itemCat);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }
}
