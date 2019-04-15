package cn.itcast.core.controller;


import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.SeckillOrderVo;

import java.util.List;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {
    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/findAll")
    public List<SeckillOrderVo> findAll(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillOrderService.findAll(name);
    }
}
