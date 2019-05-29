package cn.itcast.core.service.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TypeTemplateService
 * @Description 模板的接口
 * @Author 传智播客
 * @Date 9:57 2019/5/5
 * @Version 2.1
 **/
public interface TypeTemplateService {

    /**
     * @author 栗子
     * @Description 模板列表查询
     * @Date 9:57 2019/5/5
     * @param page
     * @param rows
     * @param typeTemplate
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    /**
     * @author 栗子
     * @Description 保存模板
     * @Date 11:04 2019/5/5
     * @param typeTemplate
     * @return void
     **/
    void add(TypeTemplate typeTemplate);

    /**
     * @author 栗子
     * @Description 通过模板id获取品牌以及扩展属性
     * @Date 11:21 2019/5/8
     * @param id
     * @return cn.itcast.core.pojo.template.TypeTemplate
     **/
    TypeTemplate findOne(Long id);

    /**
     * @author 栗子
     * @Description 通过模板id获取规格以及规格选项结果集
     * @Date 11:35 2019/5/8
     * @param id
     * @return java.util.List<java.util.Map>
     **/
    List<Map> findBySpecList(Long id);

    /**
     * @author 栗子
     * @Description 新增分类：加载商品模板
     * @Date 16:39 2019/5/22
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    List<TypeTemplate> findAll();

}
