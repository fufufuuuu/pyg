package cn.itcast.core.service.staticpage;

/**
 * @ClassName StaticPageService
 * @Description 静态化服务接口
 * @Author 传智播客
 * @Date 16:53 2019/5/15
 * @Version 2.1
 **/
public interface StaticPageService {

    /**
     * @author 栗子
     * @Description 生成商品详情的静态页
     * @Date 16:53 2019/5/15
     * @param id
     * @return void
     **/
    void getHtml(Long id);
}
