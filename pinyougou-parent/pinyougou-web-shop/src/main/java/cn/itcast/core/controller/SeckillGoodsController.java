package cn.itcast.core.controller;


import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Reference
    private SeckillGoodsService seckillGoodsService;

    @RequestMapping("/add")
    public Result add(@RequestBody SeckillGoods seckillGoods,@RequestBody long id){
        try {
            seckillGoodsService.add(seckillGoods,id);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
}
