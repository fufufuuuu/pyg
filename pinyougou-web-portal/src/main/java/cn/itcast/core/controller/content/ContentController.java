package cn.itcast.core.controller.content;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.service.content.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ContentController
 * @Description 前台系统加载广告数据
 * @Author 传智播客
 * @Date 15:41 2019/5/9
 * @Version 2.1
 **/
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * @author 栗子
     * @Description 加载首页大广告的轮播图
     * @Date 15:43 2019/5/9
     * @param categoryId
     * @return java.util.List<cn.itcast.core.pojo.ad.Content>
     **/
    @RequestMapping("/findByCategoryId.do")
    public List<Content> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }
}
