package cn.itcast.core.service;


import vo.SeckillOrderVo;

import java.util.List;

public interface SeckillOrderService {
    List<SeckillOrderVo> findAll(String name);
}
