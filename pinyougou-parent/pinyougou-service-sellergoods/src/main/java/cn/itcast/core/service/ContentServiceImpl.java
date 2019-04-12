package cn.itcast.core.service;

import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Content> findAll() {
        List<Content> list = contentDao.selectByExample(null);
        return list;
    }

    @Override
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Content> page = (Page<Content>) contentDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Content content) {
        contentDao.insertSelective(content);
    }

    // 要不要支持事务?
    //进入切面：DataSourceTransactionManager 连接Mysql数据   begin transation
    @Override
    public void edit(Content content) {

        //1： 先查询此广告是什么类型
        Content c = contentDao.selectByPrimaryKey(content.getId());
        //2:  再修改Mysql
        //修改Mysql数据库
        contentDao.updateByPrimaryKeySelective(content);  //抛异常  rollback
        //3: 判断查询出来的类型与当前页面传递过来的类型比对
        if (!c.getCategoryId().equals(content.getCategoryId())) {
            //5: 不一样修改时 改变了类型
            redisTemplate.delete(c.getCategoryId());
        }
        redisTemplate.delete(content.getCategoryId());

    }
    //进入切面：DataSourceTransactionManager 连接Mysql数据  commit

    @Override
    public Content findOne(Long id) {
        Content content = contentDao.selectByPrimaryKey(id);
        return content;
    }

    @Override
    public void delAll(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                contentDao.deleteByPrimaryKey(id);
            }
        }
    }

    //根据广告分类ID查询 广告结果集
    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        //1:查询缓存
        List<Content> contentList = (List<Content>) redisTemplate.boundValueOps(categoryId).get();
        if (null == contentList || contentList.size() == 0) {

            //3：没有 //查询数据库
            //查询数据库
            ContentQuery contentQuery = new ContentQuery();
            contentQuery.createCriteria().andCategoryIdEqualTo(categoryId);

            //排序
            contentQuery.setOrderByClause("sort_order desc");

            contentList = contentDao.selectByExample(contentQuery);
            //4:保存缓存一份  时间
            redisTemplate.boundValueOps(categoryId).set(contentList, 5, TimeUnit.HOURS);
            //redisTemplate.boundValueOps(categoryId).expire(5, TimeUnit.HOURS);

        }
        //5:返回
        return contentList;

    }

}
