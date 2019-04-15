package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillOrder;
import vo.SeckillVo;

import java.util.List;

public interface SeckillService {
    List<SeckillVo> findSeckillList(String name);
}
