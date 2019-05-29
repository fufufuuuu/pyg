package cn.itcast.core.service.brand;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BrandServiceImpl
 * @Description 品牌管理实现
 * @Author 传智播客
 * @Date 12:25 2019/4/27
 * @Version 2.1
 **/
@Service
public class BrandServiceImpl implements BrandService {

//    @Autowired  spring
    // 使用该注解好处：jdk提供
    // 1、@Autowired spring提供，框架对外提供的服务越多性能越低
    // 2、降低与框架间的耦合
    @Resource
    private BrandDao brandDao;


    /**
     * @author 栗子
     * @Description 查询所有品牌
     * @Date 12:25 2019/4/27
     * @param
     * @return java.util.List<cn.itcast.core.pojo.good.Brand>
     **/
    @Override
    public List<Brand> findAll() {
        // select id, name, first_char from tb_brand
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    /**
     * @author 栗子
     * @Description 分页查询
     * @Date 11:46 2019/4/28
     * @param pageNum
     * @param pageSize
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        // 1、设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        // 2、在查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        // 3、将数据封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @author 栗子
     * @Description 条件查询
     * @Date 12:17 2019/4/28
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        // 1、设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        // 2、设置查询条件
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if(brand != null){
            if(!StringUtils.isEmpty(brand.getName())){
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if(!StringUtils.isEmpty(brand.getFirstChar())){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        // 根据字段排序
        brandQuery.setOrderByClause("id desc");
        // 自己封装多个条件：select * from table where condition1 like ? and condition2 = ? and con.....
        // 3、再查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        // 4、将数据封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @author 栗子
     * @Description 保存品牌
     * @Date 12:50 2019/4/28
     * @param brand
     * @return void
     **/
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * @author 栗子
     * @Description 回显品牌
     * @Date 9:51 2019/4/29
     * @param id
     * @return cn.itcast.core.pojo.good.Brand
     **/
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * @author 栗子
     * @Description 更新品牌
     * @Date 9:56 2019/4/29
     * @param brand
     * @return void
     **/
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * @author 栗子
     * @Description 删除品牌
     * @Date 10:18 2019/4/29
     * @param ids
     * @return void
     **/
    @Transactional
    @Override
    public void delete(Long[] ids) {
        // 前端校验：提高用户的体验
        // 服务器端校验：保证数据的安全性
        if(ids != null && ids.length > 0){
//            for (Long id : ids) {
//                brandDao.deleteByPrimaryKey(id);
//            }
            brandDao.deleteByPrimaryKeys(ids); // 批量删除
        }
    }

    /**
     * @author 栗子
     * @Description 新增模板需要的品牌下拉框的列表数据
     * @Date 10:56 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
