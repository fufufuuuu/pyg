package cn.itcast.core.controller.manager;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.manager.ManagerServcie;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地址管理
 *
 * @author YangZiFan hujunzheng
 * @create 2019-05-30 9:07
 **/
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Reference
    private ManagerServcie managerServcie;
    /**
     * 添加地址
     * @param address
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Address address){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            managerServcie.add(name,address);
            return new Result(true,"添加成功！");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"修改失败！");
    }

    /**
     * 修改地址
     * @param address
     * @return
     */
    @RequestMapping("/update")
    public Result update(Address address){
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            managerServcie.update(userName,address);
            return new Result(true,"修改成功!");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"修改失败！");
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long id){
        try {
            managerServcie.delete(id);
            return new Result(true,"删除成功！");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new Result(false,"删除失败！");
    }

    /**
     * 地址信息回显
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Address> findByOne(){
        //从spring-security容器中获取登录的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return managerServcie.findByOne(userName);
    }
} 
