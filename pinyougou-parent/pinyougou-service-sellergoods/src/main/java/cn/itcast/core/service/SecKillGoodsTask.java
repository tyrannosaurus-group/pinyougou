package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 秒杀的同步增量
 *
 */
@Component
public class SecKillGoodsTask {



    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    /**
     * 刷新秒杀商品
     *
     *   每分钟执行查询秒杀商品表，将符合条件的记录并且缓存中不存在的秒杀商品存入缓存   
     */
    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了任务调度" + new Date());
        //查询所有的秒杀商品键集合
        List ids = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        //查询正在秒杀的商品列表      
        // 秒杀商品的 query对象
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        //审核通过
        criteria.andStatusEqualTo("1");
        //剩余库存大于0
        criteria.andStockCountGreaterThan(0);
        //开始时间小于等于当前时间
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        //结束时间大于当前时间   
        criteria.andEndTimeGreaterThan(new Date());
        //排除缓存中已经有的商品  
        criteria.andIdNotIn(ids);
        List<SeckillGoods> seckillGoodsList = seckillGoodsDao.selectByExample(seckillGoodsQuery);
        //装入缓存
        for (SeckillGoods seckill : seckillGoodsList) {
            redisTemplate.boundHashOps("seckillGoods").put(seckill.getId(), seckill);
        }
        System.out.println("将" + seckillGoodsList.size() + "条商品装入缓存");
    }


    /**
     *  移除秒杀商品
     * 每秒中在缓存的秒杀商品列表中查询过期的商品，发现过期同步到数据库，并在缓存中移除该秒杀商品
     *      
     */
    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods() {

        System.out.println("移除秒杀商品任务在执行");

        //扫描缓存中秒杀商品列表，发现过期的移除

        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();

        for (SeckillGoods seckill : seckillGoodsList) {
            if (seckill.getEndTime().getTime() < new Date().getTime()) {//如果结束日期小于当前日期，则表示过期
                seckillGoodsDao.updateByPrimaryKey(seckill);//向数据库保存记录

                redisTemplate.boundHashOps("seckillGoods").delete(seckill.getId());//移除缓存数据
                System.out.println("移除秒杀商品" + seckill.getId());
            }
        }
        System.out.println("移除秒杀商品任务结束");

    }
}
