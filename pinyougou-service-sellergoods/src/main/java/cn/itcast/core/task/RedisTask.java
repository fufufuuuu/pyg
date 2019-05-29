package cn.itcast.core.task;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RedisTask
 * @Description 定时任务
 * @Author 传智播客
 * @Date 18:25 2019/5/14
 * @Version 2.1
 **/
@Component  // 注入bean
public class RedisTask {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    // 将商品分类的数据写入缓存
    // cron:时间表达式，指定该程序的执行时间
    @Scheduled(cron = "00 42 18 * * ?")
    public void autoDataToRedisForItemCat(){
        // 将商品分类的数据写入缓存中
        List<ItemCat> list = itemCatDao.selectByExample(null);
        if(list != null && list.size() > 0){
            for (ItemCat itemCat : list) {
                // key:分类名称   value:模板id
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
            }
        }
        System.out.println("111定时器执行啦。。。");
    }

    // 将商品模板的数据写入缓存
    @Scheduled(cron = "00 42 18 * * ?")
    public void autoDataToRedisForTemplate(){
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
        System.out.println("222定时器执行啦。。。");
    }

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
}
