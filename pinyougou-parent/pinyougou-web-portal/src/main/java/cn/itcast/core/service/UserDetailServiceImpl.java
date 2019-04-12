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
 * 自定义授权实现类
 */
public class UserDetailServiceImpl implements UserDetailsService{


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //用户名 授权

        Set<GrantedAuthority> authorities = new HashSet<>();
        //从5张表 角色表
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username,"",authorities);
    }
}
