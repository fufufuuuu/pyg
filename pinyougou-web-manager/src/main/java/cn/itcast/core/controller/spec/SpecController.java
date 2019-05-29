package cn.itcast.core.controller.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.spec.SpecService;
import cn.itcast.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SpecController
 * @Description 规格管理
 * @Author 传智播客
 * @Date 12:14 2019/4/29
 * @Version 2.1
 **/
@RestController
@RequestMapping("/specification")
public class SpecController {


    @Reference
    private SpecService specService;

    /**
     * @author 栗子
     * @Description 规格列表查询
     * @Date 12:16 2019/4/29
     * @param page
     * @param rows
     * @param specification
     * @return cn.itcast.core.entity.PageResult
     **/
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Specification specification){
        return specService.search(page, rows, specification);
    }

    /**
     * @author 栗子
     * @Description 保存规格
     * @Date 12:35 2019/4/29
     * @param specVo
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/add.do")
    public Result add(@RequestBody SpecVo specVo){
        try {
            specService.add(specVo);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }

    /**
     * @author 栗子
     * @Description 回显规格
     * @Date 9:20 2019/5/5
     * @param id
     * @return cn.itcast.core.vo.SpecVo
     **/
    @RequestMapping("/findOne.do")
    public SpecVo findOne(Long id){
        return specService.findOne(id);
    }

    /**
     * @author 栗子
     * @Description 更新规格
     * @Date 9:21 2019/5/5
     * @param specVo
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/update.do")
    public Result update(@RequestBody SpecVo specVo){
        try {
            specService.update(specVo);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "更新失败");
    }

    /**
     * @author 栗子
     * @Description 删除规格
     * @Date 9:29 2019/5/5
     * @param ids
     * @return cn.itcast.core.entity.Result
     **/
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            specService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }

    /**
     * @author 栗子
     * @Description 新增模板需要的规格下拉框的列表数据
     * @Date 10:59 2019/5/5
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    @RequestMapping("/selectOptionList.do")
    public List<Map<String, String>> selectOptionList(){
        return specService.selectOptionList();
    }

}
