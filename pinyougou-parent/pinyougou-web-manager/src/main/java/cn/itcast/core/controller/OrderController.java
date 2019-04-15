package cn.itcast.core.controller;

import cn.itcast.core.service.FindOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderVo;

import java.util.List;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @Reference
    private FindOrderService findOrderService;

    //提交订单
    @RequestMapping("/search")
    public PageResult search(Integer pageNo, Integer pageSize, @RequestBody OrderVo orderVo) {
        PageResult search = findOrderService.search(pageNo, pageSize,orderVo);
        return search;
    }
}
