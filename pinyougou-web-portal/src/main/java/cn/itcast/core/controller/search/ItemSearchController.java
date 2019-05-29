package cn.itcast.core.controller.search;

import cn.itcast.core.service.search.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName ItemSearchController
 * @Description 商品检索
 * @Author 传智播客
 * @Date 16:57 2019/5/13
 * @Version 2.1
 **/
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    /**
     * @author 栗子
     * @Description 前台系统检索
     * @Date 16:59 2019/5/13
      * @param searchMap
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/search.do")
    public Map<String, Object> search(@RequestBody Map<String, String> searchMap){
        return itemSearchService.search(searchMap);
    }

}
