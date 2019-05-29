package cn.itcast.core.controller.addr;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.addr.AddrService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName AddrController
 * @Description 收件人地址
 * @Author 传智播客
 * @Date 12:11 2019/5/21
 * @Version 2.1
 **/
@RestController
@RequestMapping("/address")
public class AddrController {

    @Reference
    private AddrService addrService;

    /**
     * @author 栗子
     * @Description 当前收件人地址列表
     * @Date 12:12 2019/5/21
     * @param
     * @return java.util.List<cn.itcast.core.pojo.address.Address>
     **/
    @RequestMapping("/findListByLoginUser.do")
    public List<Address> findListByLoginUser(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return addrService.findListByLoginUser(userId);
    }
}
