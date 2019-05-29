package cn.itcast.core.controller.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.temp.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName TypeTemplateController
 * @Description 模板管理
 * @Author 传智播客
 * @Date 10:02 2019/5/5
 * @Version 2.1
 **/
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * @author 栗子
     * @Description 模板列表查询
     * @Date 10:03 2019/5/5
     * @param page
     * @param rows
     * @param typeTemplate
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){
        return typeTemplateService.search(page, rows, typeTemplate);
    }

    /**
     * @author 栗子
     * @Description 保存模板
     * @Date 11:05 2019/5/5
     * @param typeTemplate
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }

    /**
     * @author 栗子
     * @Description 新增分类：加载商品模板
     * @Date 16:41 2019/5/22
      * @param
     * @return java.util.List<cn.itcast.core.pojo.template.TypeTemplate>
     **/
    @RequestMapping("/findAll.do")
    public List<TypeTemplate> findAll(){
        return typeTemplateService.findAll();
    }
}
