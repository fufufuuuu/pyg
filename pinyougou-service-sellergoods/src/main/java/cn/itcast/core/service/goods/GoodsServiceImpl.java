package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.staticpage.StaticPageService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName GoodsServiceImpl
 * @Description 商品服务
 * @Author 传智播客
 * @Date 11:37 2019/5/6
 * @Version 2.1
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private BrandDao brandDao;

    @Resource
    private SolrTemplate solrTemplate;

//    @Resource
//    private StaticPageService staticPageService;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicPageAndSolrDestination;

    @Resource
    private Destination queueSolrDeleteDestination;

    /**
     * @author 栗子
     * @Description 商品保存
     * @Date 11:38 2019/5/6
      * @param goodsVo
     * @return void
     **/
    @Transactional
    @Override
    public void add(GoodsVo goodsVo) {
        // 1、保存商品的基本信息
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");  // 商品的审核状态  0:待审核  1:审核通过 2：审核未通过
//        goods.setSellerId(sllerId);
        goodsDao.insertSelective(goods); // (返回自增主键的id)
        // 2、保存商品的描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);
        // 3、保存商品对应的库存信息
        // 判断是否启用规格
        if("1".equals(goods.getIsEnableSpec())){    // 启用规格
            //  1 spu ---> n sku
            List<Item> itemList = goodsVo.getItemList();    // 库存
            if(itemList != null && itemList.size() > 0){
                for (Item item : itemList) {
                    // 商品标题=spu名称+spu副标题+规格名称
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    // 栗子：   spec:{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();
                    // json串--->对象
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);

                    setItemAttribute(goods, goodsDesc, item);

                    // 保存
                    itemDao.insertSelective(item);
                }
            }
        }else{  // 未启用规格
            // 1 spu ---> 1 sku
            Item item = new Item();
            String title = goods.getGoodsName() + " " + goods.getCaption();
            item.setTitle(title);
            item.setStatus("1");
            item.setIsDefault("1");
            item.setPrice(goods.getPrice());
            item.setNum(9999);  // 不合理
            setItemAttribute(goods, goodsDesc, item);
            itemDao.insertSelective(item);
        }

    }

    /**
     * @author 栗子
     * @Description 商家系统的商品列表查询
     * @Date 10:17 2019/5/9
     * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件：该商家
        GoodsQuery goodsQuery = new GoodsQuery();
        if(goods.getSellerId() != null && !"".equals(goods.getSellerId())){
            goodsQuery.createCriteria().andSellerIdEqualTo(goods.getSellerId());
        }
        // 3、查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        // 4、将结果封装到pageResult
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * @author 栗子
     * @Description 回显商品
     * @Date 11:07 2019/5/9
     * @param id
     * @return cn.itcast.core.vo.GoodsVo
     **/
    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        // 商品基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        goodsVo.setGoods(goods);
        // 商品描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsVo.setGoodsDesc(goodsDesc);
        // 商品对应的库存信息
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        goodsVo.setItemList(itemList);
        return goodsVo;
    }

    /**
     * @author 栗子
     * @Description 商品更新
     * @Date 11:12 2019/5/9
     * @param goodsVo
     * @return void
     **/
    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        // 更新商品基本信息
        Goods goods = goodsVo.getGoods();
        // 注意了，更新后的商品需要重新审核
        goods.setAuditStatus("0");  // 打回来的商品
        goodsDao.updateByPrimaryKeySelective(goods);
        // 更新商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);
        // 更新商品库存信息：
        // 先删除
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(itemQuery);
        // 再插入
        // 判断是否启用规格
        if("1".equals(goods.getIsEnableSpec())){    // 启用规格
            //  1 spu ---> n sku
            List<Item> itemList = goodsVo.getItemList();    // 库存
            if(itemList != null && itemList.size() > 0){
                for (Item item : itemList) {
                    // 商品标题=spu名称+spu副标题+规格名称
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    // 栗子：   spec:{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();
                    // json串--->对象
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);

                    setItemAttribute(goods, goodsDesc, item);

                    // 保存
                    itemDao.insertSelective(item);
                }
            }
        }else{  // 未启用规格
            // 1 spu ---> 1 sku
            Item item = new Item();
            String title = goods.getGoodsName() + " " + goods.getCaption();
            item.setTitle(title);
            item.setStatus("1");
            item.setIsDefault("1");
            item.setPrice(goods.getPrice());
            item.setNum(9999);  // 不合理
            setItemAttribute(goods, goodsDesc, item);
            itemDao.insertSelective(item);
        }
    }

    /**
     * @author 栗子
     * @Description 运营系统的商品列表查询
     * @Date 11:35 2019/5/9
     * @param page
     * @param rows
     * @param goods
     * @return cn.itcast.core.entity.PageResult
     **/
    @Override
    public PageResult searchForManager(Integer page, Integer rows, Goods goods) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件：待审核并且未删除,isDelete为null的数据
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if(goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus())){
            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
        }
        criteria.andIsDeleteIsNull();
        goodsQuery.setOrderByClause("id desc");
        // 3、查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * @author 栗子
     * @Description 商品审核
     * @Date 12:09 2019/5/9
     * @param ids
     * @param status
     * @return void
     **/
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if(ids != null && ids.length > 0){
            Goods goods = new Goods();
            goods.setAuditStatus(status);
            for (final Long id : ids) {
                // 1、更新商品的审核状态
                goods.setId(id);
                // update tb_goods set audit_status = xxx where id = xxx
                goodsDao.updateByPrimaryKeySelective(goods);
                if("1".equals(status)){
                    // 2、对该商品进行上架
//                    isShow(id);
                    // 今天：将所有的数据库的数据导入到索引库中
//                    dataImportMysqlToSolr();
                    // 3、生成该商品详情的静态页面
//                    staticPageService.getHtml(id);
                    // 将商品id发送到消息队列中
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            // 将商品id封装到消息体中
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });
                }
            }
        }

    }

    // 将商品进行上架
    private void isShow(Long id) {
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

    // 将数据库的数据导入到索引库中（清空之前索引库中的数据）
    private void dataImportMysqlToSolr() {
        // 查询库存表的数据
        ItemQuery query = new ItemQuery();
        query.createCriteria().andStatusEqualTo("1");
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
     * @Description 删除商品
     * @Date 12:21 2019/5/9
     * @param ids
     * @return void
     **/
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if(ids != null && ids.length > 0){
            Goods goods = new Goods();
            goods.setIsDelete("1");
            for (final Long id : ids) {
                goods.setId(id);
                // 1、逻辑删除
                goodsDao.updateByPrimaryKeySelective(goods);

                // 发送消息到mq中
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                        return textMessage;
                    }
                });
                // 2、商品下架
//                SimpleQuery query = new SimpleQuery("item_goodsid:"+id);
//                solrTemplate.delete(query);
//                solrTemplate.commit();
                // 3、是否删除静态页：不删除
            }
        }
    }


    // 设置库存商品公共的属性
    private void setItemAttribute(Goods goods, GoodsDesc goodsDesc, Item item) {
        // 商品图片---goods_desc(取一张)
        // [{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"},
        // {"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]
        String images = goodsDesc.getItemImages();
        List<Map> imageList = JSON.parseArray(images, Map.class);
        if(imageList != null && imageList.size() > 0){
            String image = imageList.get(0).get("url").toString();
            item.setImage(image);
        }
        // 商品分类的id(三级)
        item.setCategoryid(goods.getCategory3Id());
        // 商品的状态
//                    item.setStatus("1");
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        // 商品id
        item.setGoodsId(goods.getId());
        // 商家id
        item.setSellerId(goods.getSellerId());
        // 商品分类名称、商品品牌名称、商家店铺名称
        item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
        item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getNickName());
        item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
    }
}
