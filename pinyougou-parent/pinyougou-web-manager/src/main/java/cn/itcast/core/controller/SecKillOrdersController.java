package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SecKillGoodsService;
import cn.itcast.core.service.SecKillOrdersService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.SecKillOrdersVo;

import java.util.List;

@RestController
@RequestMapping("/secKillOrders")
public class SecKillOrdersController {
    @Reference
    private SecKillOrdersService secKillOrdersService;
    //根据条件查询 分页对象
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows , @RequestBody SeckillOrder secKillOrder){
        return  secKillOrdersService.search(page,rows,secKillOrder);
    }
    @RequestMapping("/findSecKillSeller")
    public List<String> findSecKillSeller(){
        return secKillOrdersService.findSecKillSeller();
    }

    @RequestMapping("/findOne")
    public SecKillOrdersVo findOne(Long id){
        return secKillOrdersService.findOne(id);
    }
}
