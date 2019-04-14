package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private OrderDao orderDao;


	@Override
	public void add(Order order) {

	}

	@Override
	public List<OrderItem> findAll() {
		return orderItemDao.selectByExample(null);
	}

	@Override
	public PageResult findPage(Integer pageNo, Integer pageSize) {
		//Mybatis分页插件
		PageHelper.startPage(pageNo,pageSize);
		Page<OrderItem> page = (Page<OrderItem>) orderItemDao.selectByExample(null);
		//PageInfo<Brand> info = new PageInfo<>(brandList);
		return new PageResult(page.getTotal(),page.getResult());
	}


}
