package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import entity.PageResult;

import java.util.List;
import entity.PageResult;
import vo.OrderVo;
import vo.PageBean;

public interface OrderService {
    void add(Order order);

	List<OrderItem> findAll();

	PageResult findPage(Integer pageNo, Integer pageSize);

    PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name);
}
