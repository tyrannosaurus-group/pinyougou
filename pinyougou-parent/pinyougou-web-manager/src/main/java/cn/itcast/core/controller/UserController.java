package cn.itcast.core.controller;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.FindUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.UserCountVo;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private FindUserService findUserService;

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody User user) {

        return findUserService.search(page, rows, user);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status) {
        try {

            findUserService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    @RequestMapping("/userCount")
    public UserCountVo userCount() {
        return findUserService.userCount();
    }

}
