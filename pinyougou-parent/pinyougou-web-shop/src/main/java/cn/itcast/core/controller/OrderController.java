package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderVo;

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
	public List<OrderVo> findAll() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderService.findAll(name);
	}

	//搜索
	@RequestMapping("search")
	public PageResult search(Integer page, Integer rows, @RequestBody Order order,String searchDate) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.search(page, rows, name, order,searchDate);
	}

	//分页
	@RequestMapping("findPage")
	public PageResult findPage(Integer page, Integer rows) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.findPage(page, rows, name);
	}
}
