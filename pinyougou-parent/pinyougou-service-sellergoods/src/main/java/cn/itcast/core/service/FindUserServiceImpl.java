package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FindUserServiceImpl implements FindUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        PageHelper.startPage(page, rows);

        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if (null != user.getUsername() && !"".equals(user.getUsername().trim())) {
            criteria.andUsernameLike("%" + user.getUsername().trim() + "%");
        }
        if (null != user.getStatus() && !"".equals(user.getStatus())) {
            criteria.andStatusEqualTo(user.getStatus());
        }

        Page<User> userPage= (Page<User>) userDao.selectByExample(userQuery);

        return new PageResult(userPage.getTotal(),userPage.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        if (null != ids && ids.length > 0) {
            for (Long id : ids) {

                User user = new User();
                user.setId(id);
                user.setStatus(status);

                userDao.updateByPrimaryKeySelective(user);
            }
        }
    }
}
