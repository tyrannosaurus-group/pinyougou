package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Reference
    private CollectService collectService;

    @RequestMapping("/findList")
    public List<OrderItem> findList(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return collectService.findList(name);
    }
}
