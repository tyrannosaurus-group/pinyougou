package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import entity.PageResult;
import vo.UserCountVo;

public interface FindUserService {
    PageResult search(Integer page, Integer rows, User user);

    void updateStatus(Long[] ids, String status);

    UserCountVo userCount();
}
