package cn.itcast.core.controller.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.brand.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BrandController
 * @Description 品牌管理
 * @Author 传智播客
 * @Date 12:32 2019/4/27
 * @Version 2.1
 **/
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * @author 栗子
     * @Description 查询所有品牌
     * @Date 12:33 2019/4/27
     * @param
     * @return java.util.List<cn.itcast.core.pojo.good.Brand>
     **/
    @RequestMapping("/findAll.do")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    /**
     * @author 栗子
     * @Description 分页查询
     * @Date 11:49 2019/4/28
     * @param pageNum
     * @param pageSize
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer pageNum, Integer pageSize){
        return brandService.findPage(pageNum, pageSize);
    }

    /**
     * @author 栗子
     * @Description 条件查询
     * @Date 12:27 2019/4/28
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody Brand brand){
        return brandService.search(pageNum, pageSize, brand);
    }

    /**
     * @author 栗子
     * @Description 保存品牌
     * @Date 12:53 2019/4/28
     * @param brand
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }

    /**
     * @author 栗子
     * @Description 回显品牌
     * @Date 9:52 2019/4/29
     * @param id
     * @return cn.itcast.core.pojo.good.Brand
     **/
    @RequestMapping("/findOne.do")
    public Brand findOne(Long id){
        return brandService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 更新品牌
     * @Date 9:58 2019/4/29
     * @param brand
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "更新失败");
    }

    /**
     * @author 栗子
     * @Description 删除品牌
     * @Date 10:20 2019/4/29
     * @param ids
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }

    /**
     * @author 栗子
     * @Description 新增模板需要的品牌下拉框的列表数据
     * @Date 10:56 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    @RequestMapping("/selectOptionList.do")
    public List<Map<String, String>> selectOptionList(){
        return brandService.selectOptionList();
    }

}
