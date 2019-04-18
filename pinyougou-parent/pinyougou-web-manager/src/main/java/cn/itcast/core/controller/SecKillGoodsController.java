package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SecKillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/secKillGoods")
public class SecKillGoodsController {
    @Reference
    private SecKillGoodsService secKillGoodsService;
    //根据条件查询 分页对象
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows , @RequestBody SeckillGoods seckillGoods){
        //运营商后台管理商品的时候 对所有商家进行统一处理
        return  secKillGoodsService.search(page,rows,seckillGoods);
    }
    @RequestMapping("/findSecKillSeller")
    public List<String> findSecKillSeller(){
        return secKillGoodsService.findSecKillSeller();
    }

    ///开始审核
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status){
        try {
            secKillGoodsService.updateStatus(ids,status);
            return new Result(true,"审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }

    @RequestMapping("/findOne")
    public SeckillGoods findOne(Long id){
        return secKillGoodsService.findOne(id);
    }
}
