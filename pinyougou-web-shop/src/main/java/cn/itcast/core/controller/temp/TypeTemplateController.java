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
import java.util.Map;

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
     * @Description 通过模板id获取品牌以及扩展属性
     * @Date 11:23 2019/5/8
      * @param id
     * @return cn.itcast.core.pojo.template.TypeTemplate
     **/
    @RequestMapping("/findOne.do")
    public TypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 通过模板id获取规格以及规格选项结果集
     * @Date 11:43 2019/5/8
     * @param id
     * @return java.util.List<java.util.Map>
     **/
    @RequestMapping("/findBySpecList.do")
    public List<Map> findBySpecList(Long id){
        return typeTemplateService.findBySpecList(id);
    }
}
