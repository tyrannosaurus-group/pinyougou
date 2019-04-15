package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Override
    public void add(SeckillGoods seckillGoods,long id) {
        seckillGoods.setGoodsId(id);
        seckillGoods.setCreateTime(new Date());
        seckillGoods.setStatus("0");
        seckillGoodsDao.insert(seckillGoods);

    }
}
