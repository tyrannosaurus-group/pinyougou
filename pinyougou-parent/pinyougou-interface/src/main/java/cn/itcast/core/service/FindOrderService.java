package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;
import vo.OrderCountVo;
import vo.OrderVo;
import vo.UserOrderVo;

import java.util.List;
import java.util.Map;

public interface FindOrderService {

    PageResult search(Integer pageNo, Integer pageSize, OrderVo orderVo);
    Map<String,List<Order>> findOrders();

    OrderCountVo orderCount();

    PageResult download(Integer pageNo, Integer pageSize,UserOrderVo vo);

    List<UserOrderVo> findAllUserOrders();
}
