package cn.itcast.core.service.temp;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TypeTemplateServiceImpl
 * @Description 模板服务
 * @Author 传智播客
 * @Date 9:58 2019/5/5
 * @Version 2.1
 **/
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @author 栗子
     * @Description 模板列表查询
     * @Date 9:59 2019/5/5
     * @param page
     * @param rows
     * @param typeTemplate
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {

        // 将模板的数据写入缓存
        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if(list != null && list.size() > 0){
            for (TypeTemplate template : list) {
                // 模板id---品牌结果集
                // json串：[{"id":41,"text":"奥义"},{"id":42,"text":"金啦啦"}]
                String brandIds = template.getBrandIds();
                List<Map> brandList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps("brandList").put(template.getId(), brandList);
                // 模板id---规格结果集
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(), specList);
            }
        }


        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        TypeTemplateQuery query = new TypeTemplateQuery();
        if(typeTemplate.getName() != null && !"".equals(typeTemplate.getName())){
            query.createCriteria().andNameLike("%" + typeTemplate.getName() + "%");
        }
        query.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<TypeTemplate> p = (Page<TypeTemplate>) typeTemplateDao.selectByExample(query);
        // 4、封装结果
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * @author 栗子
     * @Description 保存模板
     * @Date 11:04 2019/5/5
     * @param typeTemplate
     * @return void
     **/
    @Transactional
    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    /**
     * @author 栗子
     * @Description 通过模板id获取品牌以及扩展属性
     * @Date 11:22 2019/5/8
     * @param id
     * @return cn.itcast.core.pojo.template.TypeTemplate
     **/
    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    /**
     * @author 栗子
     * @Description 通过模板id获取规格以及规格选项结果集
     * @Date 11:35 2019/5/8
     * @param id
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map> findBySpecList(Long id) {
        // 通过模板id获取模板对象
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        // 获取规格结果集
        // 数据：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        // 将json串转成对象
        List<Map> specList = JSON.parseArray(specIds, Map.class);
        // specList:[{id:xxx,text:xxx}....]
        if(specList != null && specList.size() > 0){
            for (Map map : specList) {
                String specId = map.get("id").toString();
                // 通过规格的id获取对应的规格选项
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                query.createCriteria().andSpecIdEqualTo(Long.parseLong(specId));
                List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
                // 封装到map
                map.put("options", options);
            }
        }
        // specList:[{id:xxx,text:xxx,options:[{}....]}....]
        return specList;
    }

    /**
     * @author 栗子
     * @Description 新增分类：加载商品模板
     * @Date 16:40 2019/5/22
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @Override
    public List<TypeTemplate> findAll() {
        return typeTemplateDao.selectByExample(null);
    }
}
