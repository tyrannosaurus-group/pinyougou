package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义授权实现类
 */
@SuppressWarnings("all")
public class UserDetailServiceImpl implements UserDetailsService{


    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //用户名 授权
        User user = userService.findUser(username);
        if (user != null) {
            if (user.getStatus().equals("1")) {

                Set<GrantedAuthority> authorities = new HashSet<>();
                //从5张表 角色表
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new org.springframework.security.core.userdetails.User(username, "", authorities);
            }
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        //如果查询的用户状态不对的话，只要不赋予登陆权限就可以

        authorities.add(new SimpleGrantedAuthority("ROLE_USER12"));
        return new org.springframework.security.core.userdetails.User(username, "", authorities);

    }
}
