package cn.itcast.core.service.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BrandService
 * @Description 品牌管理接口
 * @Author 传智播客
 * @Date 12:23 2019/4/27
 * @Version 2.1
 **/
public interface BrandService {

    /**
     * @author 栗子
     * @Description 查询所有的品牌
     * @Date 12:24 2019/4/27
     * @param
     * @return java.util.List<cn.itcast.core.pojo.good.Brand>
     **/
    List<Brand> findAll();

    /**
     * @author 栗子
     * @Description 分页查询
     * @Date 11:45 2019/4/28
     * @param pageNum
     * @param pageSize
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult findPage(Integer pageNum, Integer pageSize);

    /**
     * @author 栗子
     * @Description 条件查询
     * @Date 12:17 2019/4/28
     * @param pageNum
     * @param pageSize
     * @param brand 封装查询条件
     * @return cn.itcast.core.entity.PageResult
     **/
    PageResult search(Integer pageNum, Integer pageSize, Brand brand);

    /**
     * @author 栗子
     * @Description 保存品牌
     * @Date 12:50 2019/4/28
     * @param brand
     * @return void
     **/
    void add(Brand brand);

    /**
     * @author 栗子
     * @Description 回显品牌
     * @Date 9:50 2019/4/29
     * @param id
     * @return cn.itcast.core.pojo.good.Brand
     **/
    Brand findOne(Long id);

    /**
     * @author 栗子
     * @Description 更新品牌
     * @Date 9:56 2019/4/29
     * @param brand
     * @return void
     **/
    void update(Brand brand);

    /**
     * @author 栗子
     * @Description 删除品牌
     * @Date 10:17 2019/4/29
     * @param ids
     * @return void
     **/
    void delete(Long[] ids);

    /**
     * @author 栗子
     * @Description 新增模板需要的品牌下拉框的列表数据
     * @Date 10:56 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String,String>> selectOptionList();
}
