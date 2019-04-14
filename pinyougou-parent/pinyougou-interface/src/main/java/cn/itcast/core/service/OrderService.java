package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;
import vo.OrderVo;
import vo.PageBean;

import java.util.List;

public interface OrderService {
    void add(Order order);

    List<OrderVo> findAll(String name);

    PageResult search(Integer page, Integer rows, String name, Order order, String searchDate);

    PageResult findPage(Integer page, Integer rows, String name);

    PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name);
}
