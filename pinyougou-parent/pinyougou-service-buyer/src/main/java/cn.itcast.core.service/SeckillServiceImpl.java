package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsDescQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import vo.SeckillVo;

import java.util.List;

public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;

    @Override
    public List<SeckillVo> findSeckillList(String name) {
        List<SeckillVo> list = redisTemplate.boundListOps("seckillOrder"+name).range(0,-1);
        if (null == list && list.size() == 0) {
            SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
            seckillOrderQuery.createCriteria().andUserIdEqualTo(name);
            List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(seckillOrderQuery);
            for (SeckillOrder seckillOrder : seckillOrders) {
                SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillOrder.getSeckillId());
                GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(seckillGoods.getGoodsId());
                SeckillVo seckillVo = new SeckillVo();
                seckillVo.setId(seckillOrder.getId());
                seckillVo.setMoney(seckillOrder.getMoney());
                seckillVo.setPrice(seckillGoods.getPrice());
                seckillVo.setCostPrice(seckillGoods.getCostPrice());
                seckillVo.setNum(seckillVo.getMoney().divide(seckillVo.getCostPrice()).intValue());
                seckillVo.setSellerId(seckillOrder.getSellerId());
                seckillVo.setStatus(seckillOrder.getStatus());
                seckillVo.setGoodsDesc(goodsDesc);
                list.add(seckillVo);
            }

            redisTemplate.boundListOps("seckillOrder"+name).leftPushAll(list);

        }
        return list;


    }
}
