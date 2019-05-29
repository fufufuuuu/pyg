package cn.itcast.core.service.spec;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SpecServiceImpl
 * @Description 规格实现
 * @Author 传智播客
 * @Date 12:08 2019/4/29
 * @Version 2.1
 **/
@Service
public class SpecServiceImpl implements SpecService {

    @Resource
    private SpecificationDao specificationDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    /**
     * @author 栗子
     * @Description 列表查询
     * @Date 12:09 2019/4/29
     * @param page
     * @param rows
     * @param specification
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件 + 根据id排序
        SpecificationQuery query = new SpecificationQuery();
        if(specification != null){
            if(!StringUtils.isEmpty(specification.getSpecName())){
                query.createCriteria().andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }
        query.setOrderByClause("id desc");
        // 3、查询
        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(query);
        // 4、将结果封装到PageResult中
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * @author 栗子
     * @Description 保存规格
     * @Date 12:29 2019/4/29
     * @param specVo
     * @return void
     **/
    @Transactional
    @Override
    public void add(SpecVo specVo) {
        // 保存规格（保存完成后，需要返回自增主键的id）
        Specification specification = specVo.getSpecification();
        specificationDao.insertSelective(specification);    // 返回id
        // 保存规格选项（外键：spec_id）
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if(specificationOptionList != null && specificationOptionList.size() > 0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                // 设置外键
                specificationOption.setSpecId(specification.getId());
                // 保存：一个个保存的
//                specificationOptionDao.insertSelective(specificationOption);
            }
            // 批量插入
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    /**
     * @author 栗子
     * @Description 回显规格以及规格选项
     * @Date 9:15 2019/5/5
     * @param id
     * @return cn.itcast.core.vo.SpecVo
     **/
    @Override
    public SpecVo findOne(Long id) {
        // 查询规格
        Specification specification = specificationDao.selectByPrimaryKey(id);
        // 查询规格选项
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        List<SpecificationOption> specificationOptionList = specificationOptionDao.selectByExample(query);
        // 封装到vo
        SpecVo specVo = new SpecVo();
        specVo.setSpecification(specification);
        specVo.setSpecificationOptionList(specificationOptionList);
        return specVo;
    }

    /**
     * @author 栗子
     * @Description 更新规格
     * @Date 9:17 2019/5/5
     * @param specVo
     * @return void
     **/
    @Transactional
    @Override
    public void update(SpecVo specVo) {
        // 1、更新规格
        Specification specification = specVo.getSpecification();
        specificationDao.updateByPrimaryKeySelective(specification);
        // 2、更新规格选项
        // 先删除
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(specification.getId());
        specificationOptionDao.deleteByExample(query);
        // 再插入
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if(specificationOptionList != null && specificationOptionList.size() > 0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                // 设置外键
                specificationOption.setSpecId(specification.getId());
            }
            // 批量插入
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    /**
     * @author 栗子
     * @Description 删除规格
     * @Date 9:23 2019/5/5
     * @param ids
     * @return void
     **/
    @Transactional
    @Override
    public void delete(Long[] ids) {
        // 删除规格以及规格选项（主从表：先删从表再删主表）
        // 本项目中的数据库：并没有去设置外键的约束
        // 逻辑关联   物理关联（实际开发）

        // 删除：物理删除   逻辑删除（表：status 0：未删除 1:已删除）
        if(ids != null && ids.length > 0){
            for (Long id : ids) {
                // 删除规格选项
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                query.createCriteria().andSpecIdEqualTo(id);
                specificationOptionDao.deleteByExample(query);
                // 删除规格
                specificationDao.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * @author 栗子
     * @Description 新增模板需要的规格下拉框的列表数据
     * @Date 10:59 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    @Override
    public List<Map<String, String>> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
