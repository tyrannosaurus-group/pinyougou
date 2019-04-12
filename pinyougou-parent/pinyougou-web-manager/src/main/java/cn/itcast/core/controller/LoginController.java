package cn.itcast.core.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    //获取当前登陆人
    @RequestMapping("/showName")
    public Map<String,Object> showName(HttpSession session){
        //1:当前线程中的SecurityContext
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //2:本地Session
        SecurityContext spring_security_context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String name1 = spring_security_context.getAuthentication().getName();
        System.out.println("当前线程中的用户名："  +  name);
        System.out.println("Session中的用户名："  +  name1);
        Map<String,Object> map = new HashMap<>();
        map.put("username",name);
        map.put("curTime",new Date());
        return map;

    }
}
