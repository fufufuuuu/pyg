package cn.itcast.core.controller.addr;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.addr.AddrService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addr")
public class AddressController {
    @Reference
    private AddrService addrService;
    @RequestMapping("/findAddr.do")
    public List<Address> findAddr(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addrService.findListByLoginUser(name);
    }

    @RequestMapping("/addAddress.do")
    public Result addAddress(@RequestBody Address address){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            addrService.addAddress(address, name);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "添加失败");
        }
    }
}
