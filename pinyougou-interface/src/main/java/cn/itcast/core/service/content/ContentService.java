package cn.itcast.core.service.content;

import java.util.List;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;

public interface ContentService {

	public List<Content> findAll();

	public PageResult findPage(Content content, Integer pageNum, Integer pageSize);

	public void add(Content content);

	public void edit(Content content);

	public Content findOne(Long id);

	public void delAll(Long[] ids);

	/**
	 * @author 栗子
	 * @Description 首页大广告的轮播图数据
	 * @Date 15:36 2019/5/9
	 * @param categoryId
	 * @return java.util.List<cn.itcast.core.pojo.ad.Content>
	 **/
	List<Content> findByCategoryId(Long categoryId);

}
