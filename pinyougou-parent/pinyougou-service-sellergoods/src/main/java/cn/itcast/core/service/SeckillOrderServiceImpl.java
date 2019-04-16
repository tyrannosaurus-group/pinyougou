package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import vo.SeckillOrderVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private GoodsDao goodsDao;
    @Override
    public List<SeckillOrderVo> findAll(String name) {
        List<SeckillOrderVo> list=new ArrayList<>();
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        seckillOrderQuery.createCriteria().andSellerIdEqualTo(name);
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(seckillOrderQuery);
        for (SeckillOrder seckillOrder : seckillOrders) {
            SeckillOrderVo seckillOrderVo = new SeckillOrderVo();
            seckillOrderVo.setSeckillOrder(seckillOrder);
            seckillOrderVo.setGoodsName(goodsDao.selectByPrimaryKey(seckillOrder.getId()).getGoodsName());
            list.add(seckillOrderVo);
        }
        return list;

    }
}
