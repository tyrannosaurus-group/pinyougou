package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单管理
 */
@RestController
@RequestMapping("order")
public class OrderController {

	@Reference
	private OrderService orderService;

	@RequestMapping("findAll")
	public List<OrderItem> findAll(){

		return orderService.findAll();
	}

	//分页
	@RequestMapping("findPage")
	public PageResult findPage(Integer page, Integer rows){
		//总条数  结果集

		return orderService.findPage(page,rows);
	}
}
