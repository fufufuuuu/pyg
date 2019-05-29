package cn.itcast.core.service.search;

import java.util.Map;

/**
 * @ClassName ItemSearchService
 * @Description 商品检索
 * @Author 传智播客
 * @Date 16:43 2019/5/13
 * @Version 2.1
 **/
public interface ItemSearchService {

    /**
     * @author 栗子
     * @Description 商品检索
     * @Date 16:44 2019/5/13
     * @param searchMap
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> search(Map<String, String> searchMap);

    /**
     * @author 栗子
     * @Description 商品上架
     * @Date 19:31 2019/5/15
     * @param id
     * @return void
     **/
    void saveItemToSolr(Long id);

    /**
     * @author 栗子
     * @Description 商品下架
     * @Date 9:52 2019/5/17
     * @param id
     * @return void
     **/
    void deleteItemFromSolr(Long id);
}
