package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import entity.DateXxx;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	//订单搜索
	@RequestMapping("search")
	public PageResult search(Integer page, Integer rows, @RequestBody Order order,String searchDate) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.search(page, rows, name, order,searchDate);
	}

	//订单统计  过时版
	/*@RequestMapping("searchSta")
	public PageResult searchSta(Integer page, Integer rows, @RequestBody Order order,String searchDate) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.searchSta(page, rows, name, order,searchDate);
	}*/

	//订单统计  应用版
	@RequestMapping("searchStatistics")
	public PageResult searchStatistics(Integer page, Integer rows, @RequestBody DateXxx dateXxx) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.searchStatistics(page, rows, name, dateXxx.getStartDate(),dateXxx.getEndDate());
	}

	//分页
	@RequestMapping("findPage")
	public PageResult findPage(Integer page, Integer rows) {
		//总条数  结果集
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return orderService.findPage(page, rows, name);
	}

	//销售折线图
	@RequestMapping("zheXianTu")
	public String zheXianTu(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		Map<String,List> map=orderService.zheXianTu(name);
		return JSON.toJSONString(map);
	}

	//销售折线图
	@RequestMapping("changeStatus")
	public Result changeStatus(Long id){
		try {
			orderService.changeStatus(id);
			return new Result(true,"发货成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"发货失败");
		}

	}
}
