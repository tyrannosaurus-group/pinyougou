package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import entity.PageResult;

import java.util.List;

public interface SecKillGoodsService {
    PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods);

    List<String> findSecKillSeller();

    void updateStatus(Long[] ids, String status);

    SeckillGoods findOne(Long id);
}
