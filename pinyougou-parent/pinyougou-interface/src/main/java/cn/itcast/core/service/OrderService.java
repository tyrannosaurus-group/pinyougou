package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import entity.PageResult;
import vo.OrderVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.PageResult;
import vo.OrderVo;
import vo.PageBean;

public interface OrderService {
    void add(Order order);

	List<OrderItem> findAll();
	List<OrderVo> findAll(String name);

	PageResult findPage(Integer pageNo, Integer pageSize,String name);

	PageResult search(Integer page, Integer rows, String name, Order order,String searchDate);
	PageResult findPage(Integer pageNo, Integer pageSize);

    PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name);

	PageResult searchSta(Integer page, Integer rows, String name, Order order, String searchDate);

	Map<String,List> zheXianTu(String name);

	void changeStatus(Long id);

	PageResult searchStatistics(Integer page, Integer rows, String name, Date startDate, Date endDate);
}
