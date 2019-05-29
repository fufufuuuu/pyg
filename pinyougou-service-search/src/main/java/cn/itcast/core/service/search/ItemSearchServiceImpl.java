package cn.itcast.core.service.search;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName ItemSearchServiceImpl
 * @Description 商品检索实现
 * @Author 传智播客
 * @Date 16:44 2019/5/13
 * @Version 2.1
 **/
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Resource
    private SolrTemplate solrTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemDao itemDao;

    /**
     * @author 栗子
     * @Description 商品检索
     * @Date 16:45 2019/5/13
     * @param searchMap
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Scheduled
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        // 创建一个大map，封装所有的结果
        Map<String, Object> resultMap = new HashMap<>();
        // 处理关键字中包含的空格问题
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            keywords = keywords.replace(" ", "");
            searchMap.put("keywords", keywords);
        }
        // 根据关键字检索商品的列表
//        Map<String, Object> map = searchForPage(searchMap);
        // 根据关键字检索商品的列表并且关键字高亮显示
        Map<String, Object> map = searchForHighLightPage(searchMap);
        // 加载商品分类列表  list
        List<String> categoryList = searchForGroupPage(searchMap);
        if(categoryList != null && categoryList.size() > 0){
            resultMap.put("categoryList", categoryList);

            // 商品品牌列表、规格列表(默认加载第一个分类下的品牌以及规格结果集)
            Map<String, Object> brandAndSpecMap = selectBrandsAndSpecsByItemCatNameWith0(categoryList.get(0));
            resultMap.putAll(brandAndSpecMap);
        }

        resultMap.putAll(map);
        return resultMap;
//        return map;
    }

    /**
     * @author 栗子
     * @Description 商品上架
     * @Date 19:32 2019/5/15
     * @param id
     * @return void
     **/
    @Override
    public void saveItemToSolr(Long id) {
        // 查询该商品对应的库存信息
        ItemQuery query = new ItemQuery();
        query.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1")
                .andIsDefaultEqualTo("1").andNumGreaterThan(0);
        List<Item> items = itemDao.selectByExample(query);
        // 将数据导入到索引库中
        if(items != null && items.size() > 0){
            // 拼接动态字段
            for (Item item : items) {
                // {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    /**
     * @author 栗子
     * @Description 商品下架
     * @Date 9:53 2019/5/17
     * @param id
     * @return void
     **/
    @Override
    public void deleteItemFromSolr(Long id) {
        SimpleQuery query = new SimpleQuery("item_goodsid:"+id);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    // 默认加载第一个分类下的品牌以及规格结果集
    private Map<String,Object> selectBrandsAndSpecsByItemCatNameWith0(String itemCatName) {
        // 通过分类名称获取模板id
        Object typeId = redisTemplate.boundHashOps("itemCat").get(itemCatName);
        // 通过模板id获取品牌结果集、规格结果集
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
        Map<String,Object> map = new HashMap<>();
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    // 加载商品分类列表
    private List<String> searchForGroupPage(Map<String, String> searchMap) {
        // 1、设置查询条件
        Criteria criteria = new Criteria("item_keywords");   // 根据哪个字段检索
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);
        // 2、设置分组条件
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        // 3、分组查询
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);
        // 处理分组结果集
        // 4、封装数据
        List<String> list = new ArrayList<>();
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String value = groupEntry.getGroupValue();
            list.add(value);
        }

        return list;
    }

    // 根据关键字检索商品的列表并且关键字高亮显示
    private Map<String,Object> searchForHighLightPage(Map<String, String> searchMap) {
        // 1、封装检索条件
        Criteria criteria = new Criteria("item_keywords");   // 根据哪个字段检索
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        // 2、封装分页条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer start = (pageNo - 1) * pageSize;
        query.setOffset(start);    // 其始行 = (当前页码 - 1) * 每页显示的条数
        query.setRows(pageSize);       // 每页显示的条数
        // 3、封装高亮条件
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");    // 如果该字段中包含检索的关键字，那么该关键字高亮显示
        highlightOptions.setSimplePrefix("<font color='red'>");       // 开始标签
        highlightOptions.setSimplePostfix("</font>");      // 结束标签
        query.setHighlightOptions(highlightOptions);

        // 添加过滤的条件：商品的分类、品牌、规格、价格
        String category = searchMap.get("category");
        if(category != null && !"".equals(category)){
            // 添加商品分类的过滤
            Criteria cri = new Criteria("item_category");
            cri.is(category);
            SimpleFilterQuery fq = new SimpleFilterQuery(cri);
            query.addFilterQuery(fq);
        }
        String brand = searchMap.get("brand");
        if(brand != null && !"".equals(brand)){
            // 添加商品品牌的过滤
            Criteria cri = new Criteria("item_brand");
            cri.is(brand);
            SimpleFilterQuery fq = new SimpleFilterQuery(cri);
            query.addFilterQuery(fq);
        }
        String price = searchMap.get("price");
        if(price != null && !"".equals(price)){
            // 添加商品价格的过滤
            String[] prices = price.split("-");
            Criteria cri = new Criteria("item_price");
            if(price.contains("*")){    // xxx以上
                cri.greaterThan(prices[0]);
            }else{  // min-max
                cri.between(prices[0], prices[1], true, true);
            }
            SimpleFilterQuery fq = new SimpleFilterQuery(cri);
            query.addFilterQuery(fq);
        }
        String spec = searchMap.get("spec");
        if(spec != null && !"".equals(spec)){
            // 数据：{"机身内存":"16G","网络":"联通3G"}
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                Criteria cri = new Criteria("item_spec_" + entry.getKey());
                cri.is(entry.getValue());
                SimpleFilterQuery fq = new SimpleFilterQuery(cri);
                query.addFilterQuery(fq);
            }
        }

        // 添加排序条件
        // sortField：排序的字段
        // sort：排序的规则
        String sort = searchMap.get("sort");
        if(sort != null && !"".equals(sort)){
            String sortField = "item_" + searchMap.get("sortField");
            if("ASC".equals(sort)){ // 升序
                Sort s = new Sort(Sort.Direction.ASC, sortField);
                query.addSort(s);
            }else{  // 降序
                Sort s = new Sort(Sort.Direction.DESC, sortField);
                query.addSort(s);
            }
        }

        // 4、根据条件查询
        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);
        // 处理高亮显示的结果
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        if(highlighted != null && highlighted.size() > 0){
            for (HighlightEntry<Item> highlightEntry : highlighted) {
                Item item = highlightEntry.getEntity(); // 普通标题
                List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
                if(highlights != null && highlights.size() > 0){
                    String title = highlights.get(0).getSnipplets().get(0);
                    item.setTitle(title);   // 高亮标题
                }
            }
        }
        // 5、将结果封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("totalPages", highlightPage.getTotalPages());  // 总页数
        map.put("total", highlightPage.getTotalElements());    // 总条数
        map.put("rows", highlightPage.getContent());           // 结果集
        return map;
    }

    // 根据关键字检索商品的列表
    private Map<String,Object> searchForPage(Map<String, String> searchMap) {
        // 1、封装检索条件
        Criteria criteria = new Criteria("item_keywords");   // 根据哪个字段检索
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);
        // 2、封装分页条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer start = (pageNo - 1) * pageSize;
        query.setOffset(start);    // 其始行 = (当前页码 - 1) * 每页显示的条数
        query.setRows(pageSize);       // 每页显示的条数
        // 3、根据条件查询
        ScoredPage<Item> scoredPage = solrTemplate.queryForPage(query, Item.class);
//        solrTemplate.query
        // 4、将结果封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("totalPages", scoredPage.getTotalPages());  // 总页数
        map.put("total", scoredPage.getTotalElements());    // 总条数
        map.put("rows", scoredPage.getContent());           // 结果集
        return map;
    }
}
