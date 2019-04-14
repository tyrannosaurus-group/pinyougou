package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import vo.UserVo;

public interface UserService {
    void sendCode(String phone);

    void add(String smscode, User user);

    void addPersonalInfo(UserVo userVo,String name);
}
