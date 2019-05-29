package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ItemCatServiceImpl
 * @Description 商品分类实现
 * @Author 传智播客
 * @Date 10:16 2019/5/6
 * @Version 2.1
 **/
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @author 栗子
     * @Description 商品分类列表查询
     * @Date 10:17 2019/5/6
     * @param parentId
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        // 将商品分类的数据写入缓存中
//        List<ItemCat> list = itemCatDao.selectByExample(null);
//        if(list != null && list.size() > 0){
//            for (ItemCat itemCat : list) {
//                // key:分类名称   value:模板id
//                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
//            }
//        }

        // 设置查询条件
        ItemCatQuery query = new ItemCatQuery();
        query.createCriteria().andParentIdEqualTo(parentId);
        // 查询
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);
        return itemCats;
    }

    /**
     * @author 栗子
     * @Description 通过三级分类获取模板id
     * @Date 11:19 2019/5/8
     * @param id
     * @return cn.itcast.core.pojo.item.ItemCat
     **/
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    /**
     * @author 栗子
     * @Description 查询所有的分类列表
     * @Date 10:41 2019/5/9
     * @param
     * @return java.util.List<cn.itcast.core.pojo.item.ItemCat>
     **/
    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

    /**
     * @author 栗子
     * @Description 保存分类
     * @Date 16:50 2019/5/22
      * @param itemCat
     * @return void
     **/
    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }
}
