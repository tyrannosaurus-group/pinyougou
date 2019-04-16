package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类管理
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        //查询所有商品分类结果集 保存到缓存中
        List<ItemCat> itemCatList = itemCatDao.selectByExample(null);


        //遍历
        for (ItemCat itemCat : itemCatList) {

            //缓存 数据类型 Hash类型
            redisTemplate.boundHashOps("itemCatList").put(itemCat.getName(),itemCat.getTypeId());

        }
        //添加
        //修改
        //删除
        //redisTemplate.boundHashOps("itemCatList").delete(itemCat.getName())






        //从Mysql数据查询
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }

    //查询一个
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

    @Override
    public List<ItemCat> findItemCatList() {
        //从缓存中查询首页商品分类
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps("itemCat").get("indexItemCat");

        //如果缓存中没有数据，则从数据库查询再存入缓存
        if (itemCatList == null || itemCatList.size() == 0) {
            //查询出1级商品分类的集合
            List<ItemCat> itemCatList1 = findByParentId(0L);
            //遍历1级商品分类的集合
            for (ItemCat itemCat1 : itemCatList1) {
                //查询2级商品分类的集合(将1级商品分类的id作为条件)
                List<ItemCat> itemCatList2 = findByParentId(itemCat1.getId());
                //遍历2级商品分类的集合
                for (ItemCat itemCat2 : itemCatList2) {
                    //查询3级商品分类的集合(将2级商品分类的父id作为条件)
                    List<ItemCat> itemCatList3 = findByParentId(itemCat2.getId());
                    //将3级商品分类的集合封装到2级商品分类实体中
                    itemCat2.setItemCatList(itemCatList3);
                }
                /*到这一步的时候，3级商品分类已经封装到2级分类中*/
                //将2级商品分类的集合封装到1级商品分类实体中
                itemCat1.setItemCatList(itemCatList2);
            }
            //存入缓存
            redisTemplate.boundHashOps("itemCat").put("indexItemCat", itemCatList1);
            return itemCatList1;
        }
        //到这一步，说明缓存中有数据，直接返回
        return itemCatList;
    }

    @Override
    public PageResult search(Integer page, Integer rows, ItemCat itemCat) {
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        for (ItemCat itemCat1 : itemCats) {
            redisTemplate.boundHashOps("itemCatList").put(itemCat1.getName(),itemCat1.getTypeId());
        }

        PageHelper.startPage(page, rows);

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        criteria.andParentIdEqualTo(itemCat.getParentId());

        if (null != itemCat.getStatus() && !"".equals(itemCat.getStatus())) {
            criteria.andStatusEqualTo(itemCat.getStatus());
        }
        if (null != itemCat.getName() && !"".equals(itemCat.getName().trim())) {
            criteria.andNameLike("%" + itemCat.getName() + "%");
        }
        if (null != itemCat.getTypeId() && !"".equals(itemCat.getTypeId())) {
            criteria.andTypeIdEqualTo(itemCat.getTypeId());
        }

        Page<ItemCat> itemCatPage= (Page<ItemCat>) itemCatDao.selectByExample(itemCatQuery);
        return new PageResult(itemCatPage.getTotal(),itemCatPage.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        if (null != ids && ids.length > 0) {

            ItemCat itemCat = new ItemCat();
            itemCat.setStatus(status);

            for (Long id : ids) {

                itemCat.setId(id);

                itemCatDao.updateByPrimaryKeySelective(itemCat);


            }

        }
    }



    //添加
    @Override
    public void add(ItemCat itemCat) {
         itemCatDao.insertSelective(itemCat);
    }

    //修改
    @Override
    public void update(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }
}
