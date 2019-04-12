package cn.itcast.core.controller;

import cn.itcast.common.utils.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    //发短信验证码
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {

        try {
            //合法
            if (PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                //发短信 验证码 Service
                userService.sendCode(phone);
            } else {
                return new Result(false, "手机不合法");
            }
            return new Result(true, "获取验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取验证码失败");
        }

    }

    //添加
    @RequestMapping("/add")
    public Result add(String smscode, @RequestBody User user) {
        try {
            userService.add(smscode, user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
            //预期      运行时
        }

    }
}
