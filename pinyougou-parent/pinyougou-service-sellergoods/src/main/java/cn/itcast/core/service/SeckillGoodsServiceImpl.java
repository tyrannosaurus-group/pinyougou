package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SecKillGoodsServiceImpl implements SecKillGoodsService {
    @Autowired
    private SeckillGoodsDao secKillGoodsDao;

    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page, rows);
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        seckillGoodsQuery.setOrderByClause("cost_price desc");
        if (null != seckillGoods.getStatus() && !"".equals(seckillGoods.getStatus())) {
            criteria.andStatusEqualTo(seckillGoods.getStatus());
        }
        if (null != seckillGoods.getTitle() && !"".equals(seckillGoods.getTitle().trim())) {
            criteria.andTitleLike("%" + seckillGoods.getTitle().trim() + "%");
        }
        if (null != seckillGoods.getSellerId()&&!"".equals(seckillGoods.getSellerId().trim())) {
            //商家查询
            //只能查询自己家的商品
            criteria.andSellerIdEqualTo(seckillGoods.getSellerId());
        }
        Page<SeckillGoods> seckillGoodsPage = (Page<SeckillGoods>) secKillGoodsDao.selectByExample(seckillGoodsQuery);
        return new PageResult(seckillGoodsPage.getTotal(), seckillGoodsPage.getResult());
    }

    @Override
    public List<String> findSecKillSeller() {
        List<SeckillGoods> seckillGoods = secKillGoodsDao.selectByExample(null);
        List<String> sellerList = new ArrayList<>();
        for (SeckillGoods seckillGood : seckillGoods) {
            String sellerId = seckillGood.getSellerId();
            if (!sellerList.contains(sellerId)) {
                sellerList.add(sellerId);
            }
        }
        return sellerList;
    }

    //开始审核
    //status :为1时 审核通过  2时：不通过
    @Override
    public void updateStatus(Long[] ids, String status) {
        SeckillGoods seckillGoods = new SeckillGoods();
        Date date = new Date();
        seckillGoods.setCheckTime(date);
        //状态
        seckillGoods.setStatus(status);
        for (Long id : ids) {
            seckillGoods.setId(id);
            secKillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        }

    }

    @Override
    public SeckillGoods findOne(Long id) {
        SeckillGoods seckillGoods = secKillGoodsDao.selectByPrimaryKey(id);
        return seckillGoods;
    }
}

