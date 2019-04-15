package cn.itcast.core.service;

import cn.itcast.common.utils.DateUtils;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vo.UserAnalyzeVo;
import vo.UserCountVo;

import java.util.Date;

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

    @Override
    public UserCountVo userCount() {
        UserCountVo userCountVo = new UserCountVo();

        UserQuery userQuery1 = new UserQuery();
        userQuery1.createCriteria().andSexEqualTo("1");


        userCountVo.setTotal(userDao.countByExample(null));
        userCountVo.setMaleCount(userDao.countByExample(userQuery1));

        UserQuery userQuery2 = new UserQuery();
        userQuery2.createCriteria().andSexEqualTo("0");
        userCountVo.setFemaleCount(userDao.countByExample(userQuery2));

        UserQuery userQuery3 = new UserQuery();
        userQuery3.createCriteria().andStatusEqualTo("1");
        userCountVo.setNormal(userDao.countByExample(userQuery3));

        UserQuery userQuery4 = new UserQuery();
        userQuery4.createCriteria().andStatusEqualTo("2");
        userCountVo.setFreeze(userDao.countByExample(userQuery4));

        UserQuery userQuery5 = new UserQuery();
        userQuery5.createCriteria().andUserLevelEqualTo(1);
        userCountVo.setOne(userDao.countByExample(userQuery5));

        UserQuery userQuery6 = new UserQuery();
        userQuery6.createCriteria().andUserLevelEqualTo(2);
        userCountVo.setTwo(userDao.countByExample(userQuery6));

        UserQuery userQuery7 = new UserQuery();
        userQuery7.createCriteria().andUserLevelEqualTo(3);
        userCountVo.setThree(userDao.countByExample(userQuery7));

        UserQuery userQuery8 = new UserQuery();
        userQuery8.createCriteria().andUserLevelEqualTo(4);
        userCountVo.setFour(userDao.countByExample(userQuery8));

        UserQuery userQuery9 = new UserQuery();
        userQuery9.createCriteria().andUserLevelEqualTo(5);
        userCountVo.setFive(userDao.countByExample(userQuery9));

        return userCountVo;
    }

    @Override
    public UserAnalyzeVo userAnalyze() {
        UserAnalyzeVo userAnalyzeVo = new UserAnalyzeVo();

        userAnalyzeVo.setNum(userDao.countByExample(null));

        Date[] monthStartAndEndDate = DateUtils.getMonthStartAndEndDate(new Date());

        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andLastLoginTimeBetween(monthStartAndEndDate[0], monthStartAndEndDate[1]);

        userAnalyzeVo.setActive(userDao.countByExample(userQuery));

        return userAnalyzeVo;
    }
}
