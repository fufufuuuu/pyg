package cn.itcast.core.service.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SpecService
 * @Description 规格管理
 * @Author 传智播客
 * @Date 12:06 2019/4/29
 * @Version 2.1
 **/
public interface SpecService {

    /**
     * @author 栗子
     * @Description 规格列表查询
     * @Date 12:08 2019/4/29
     * @param page
     * @param rows
     * @param specification
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult search(Integer page, Integer rows, Specification specification);

    /**
     * @author 栗子
     * @Description 保存规格
     * @Date 12:28 2019/4/29
     * @param specVo
     * @return void
     **/
    void add(SpecVo specVo);


    /**
     * @author 栗子
     * @Description 回显规格以及规格选项
     * @Date 9:14 2019/5/5
     * @param id
     * @return cn.itcast.core.vo.SpecVo
     **/
    SpecVo findOne(Long id);

    /**
     * @author 栗子
     * @Description 更新规格
     * @Date 9:14 2019/5/5
     * @param specVo
     * @return void
     **/
    void update(SpecVo specVo);

    /**
     * @author 栗子
     * @Description 删除规格
     * @Date 9:23 2019/5/5
     * @param ids
     * @return void
     **/
    void delete(Long[] ids);

    /**
     * @author 栗子
     * @Description 新增模板需要的规格下拉框的列表数据
     * @Date 10:59 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String,String>> selectOptionList();
}
