package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import vo.SecKillOrdersVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SecKillOrdersServiceImpl implements SecKillOrdersService {
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public PageResult search(Integer page, Integer rows, SeckillOrder secKillOrder) {
        PageHelper.startPage(page, rows);

        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria orderCriteria = seckillOrderQuery.createCriteria();
        if (null != secKillOrder.getStatus() && !"".equals(secKillOrder.getStatus())) {
            orderCriteria.andStatusEqualTo(secKillOrder.getStatus());
        }
        if (null != secKillOrder.getSellerId()&&!"".equals(secKillOrder.getSellerId().trim())) {
            orderCriteria.andSellerIdEqualTo(secKillOrder.getSellerId());
        }
        /*if (null!=title&&!"".equals(title.trim())){
            SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria goodsCriteria = seckillGoodsQuery.createCriteria();
            goodsCriteria.andTitleLike("%"+title.trim()+"%");
            List<SeckillGoods> seckillGoods = seckillGoodsDao.selectByExample(seckillGoodsQuery);
            List<Long> list = new ArrayList<>();
            for (SeckillGoods seckillGood : seckillGoods) {
                Long seckillId = seckillGood.getId();
                if (!list.contains(seckillId)){
                    list.add(seckillId);
                }
            }
            Collections.sort(list);
            orderCriteria.andSeckillIdBetween(list.get(0)-1,list.get(list.size()-1)+1);

        }*/


        Page<SeckillOrder> seckillOrderPage = (Page<SeckillOrder>) seckillOrderDao.selectByExample(seckillOrderQuery);
        return new PageResult(seckillOrderPage.getTotal(), seckillOrderPage.getResult());
    }

    @Override
    public List<String> findSecKillSeller() {
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(null);
        List<String> sellerList = new ArrayList<>();
        for (SeckillOrder seckillOrder : seckillOrders) {
            String sellerId = seckillOrder.getSellerId();
            if (!sellerList.contains(sellerId)) {
                sellerList.add(sellerId);
            }
        }
        return sellerList;
    }



    @Override
    public SecKillOrdersVo findOne(Long id) {
        SeckillOrder seckillOrder = seckillOrderDao.selectByPrimaryKey(id);
        Long seckillId = seckillOrder.getSeckillId();
        SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillId);
        SecKillOrdersVo secKillOrdersVo = new SecKillOrdersVo();
        secKillOrdersVo.setSeckillGoods(seckillGoods);
        secKillOrdersVo.setSeckillOrder(seckillOrder);
        return secKillOrdersVo;
    }
}

