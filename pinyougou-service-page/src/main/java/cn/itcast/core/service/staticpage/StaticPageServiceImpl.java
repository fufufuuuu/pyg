package cn.itcast.core.service.staticpage;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StaticPageServiceImpl
 * @Description 静态化服务实现
 * @Author 传智播客
 * @Date 16:54 2019/5/15
 * @Version 2.1
 **/
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemCatDao itemCatDao;

    private Configuration configuration;
    // 属性注入
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    /**
     * @author 栗子
     * @Description 生成商品详情的静态页
     * @Date 16:54 2019/5/15
     * @param id
     * @return void
     **/
    @Override
    public void getHtml(Long id) {
        try {
            // 1、创建Configuration并且指定模板的位置
            // new Configuration(version)  setTemplateLoadPath(path);  此路不通(path:硬编码了)
            // 联想：springmvc  在配置文件中指定页面的位置  --- 想：在配置文件中指定模板的位置
            // FreeMarkerConfigurer：注入FreeMarkerConfigurer，获取Configuration对象以外，可以指定模板的位置
            // 2、获取该位置下的模板
            Template template = configuration.getTemplate("item.ftl");
            // 3、准备数据（业务数据）
            Map<String, Object> dataModel = getDataModel(id);
            // file：指定静态页生成的位置：可以直接访问--->tomcat--->webapps目录--->获取项目发布的真实路径
            // request.getRealPath(pathname);   --- 此路不通
            // servletContext.getRealpath：采用此路   servletContext：需要注入  ---> spring帮助
            // localhost:8080/id.html
            String pathname = "/" + id + ".html";
            String path = servletContext.getRealPath(pathname);
            File file = new File(path);
            // 4、模板 + 数据 = 输出（带有数据的静态页）
            template.process(dataModel, new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 获取模板需要的数据
    private Map<String,Object> getDataModel(Long id) {
        Map<String,Object> map = new HashMap<>();
        // 获取商品基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        map.put("goods", goods);
        // 获取商品描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        map.put("goodsDesc", goodsDesc);
        // 获取商品分类信息
        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());
        ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());
        ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
        map.put("itemCat1", itemCat1);
        map.put("itemCat2", itemCat2);
        map.put("itemCat3", itemCat3);
        // 获取商品库存信息
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        map.put("itemList", itemList);
        return map;
    }


}
