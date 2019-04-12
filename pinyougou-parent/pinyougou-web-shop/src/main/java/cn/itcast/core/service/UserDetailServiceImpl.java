package cn.itcast.core.service;

import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * 安全框架 自定义实现类
 * 从数据库获取用户名信息
 */
public class UserDetailServiceImpl implements UserDetailsService{



    private SellerService sellerService;
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据商家名称 查询商品对象
        Seller seller = sellerService.findBySellerId(username);
        //判断是否为null  //判断状态是否为审核通过
        if(null != seller && "1".equals(seller.getStatus())){

            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

            return new User(username,seller.getPassword(),authorities);
        }

        return null;
    }
}
