package cn.itcast.core.controller;

import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderVo;
import vo.PageBean;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/findOrderList")
    public PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        PageBean<OrderVo> pageBean = orderService.findOrderList(pageNum,pageSize,name);
        return pageBean;
    }
}
