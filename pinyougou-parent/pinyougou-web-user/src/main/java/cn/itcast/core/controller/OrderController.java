package cn.itcast.core.controller;

import cn.itcast.core.service.OrderService;
import entity.PageResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/findOrderList")
    public PageResult findOrderList(Integer pageNum, Integer pageSize){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResult pageResult = orderService.findOrderList(pageNum,pageSize,name);
        return pageResult;
    }
}
