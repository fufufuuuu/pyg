package cn.itcast.core.service;

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
 * @Description 前台系统的认证类
 * @Author 传智播客
 * @Date 12:57 2019/5/18
 * @Version 2.1
 **/
public class UserDetailServiceImpl implements UserDetailsService {

    /**
     * @author 栗子
     * @Description
     * @Date 12:58 2019/5/18
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 授权
        Set<GrantedAuthority> authorities = new HashSet<>();    // 添加权限的集合
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(grantedAuthority);
        User user = new User(username, "", authorities);
        return user;
    }
}
