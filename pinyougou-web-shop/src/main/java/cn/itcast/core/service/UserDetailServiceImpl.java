package cn.itcast.core.service;

import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.seller.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName UserDetailServiceImpl
 * @Description 自定义认证类
 * @Author 传智播客
 * @Date 12:12 2019/5/6
 * @Version 2.1
 **/
public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;
    // 属性注入
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * @author 栗子
     * @Description 认证 + 授权
     * @Date 12:12 2019/5/6
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Seller seller = sellerService.findOne(username);
        // 认证用户(必须是审核通过的用户)
        if(seller != null && "1".equals(seller.getStatus())){
            // 授权
            Set<GrantedAuthority> authorities = new HashSet<>();    // 添加权限的集合
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SELLER");
            authorities.add(grantedAuthority);
            User user = new User(username, seller.getPassword(), authorities);
            return user;
        }
        return null;
    }
}
