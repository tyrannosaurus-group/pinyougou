package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import entity.PageResult;
import vo.SecKillOrdersVo;

import java.math.BigDecimal;
import java.util.List;

public interface SecKillOrdersService {
    PageResult search(Integer page, Integer rows, SeckillOrder secKillOrder);

    List<String> findSecKillSeller();

    SecKillOrdersVo findOne( Long id);
}
