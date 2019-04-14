package cn.itcast.core.controller;

import cn.itcast.core.service.OrderPayService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Cart;

import java.util.List;

/**
 * 未付款订单管理
 */
@RestController
@RequestMapping("orderPay")
public class OrderPayController {

    @Reference
    private OrderPayService orderPayService;
    @RequestMapping("findPage")
    public PageResult findPage(Integer page,Integer rows){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderPayService.findPage(name,page,rows);
    }
}
