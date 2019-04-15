package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import vo.OrderVo;
import vo.PageBean;

import java.util.ArrayList;
import java.util.List;

@Service
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private OrderDao orderDao;

	@Override
	public void add(Order order) {

	}

	@Override
	public List<OrderVo> findAll(String name) {
		OrderItemQuery query = new OrderItemQuery();
		query.createCriteria().andSellerIdEqualTo(name);
		List<OrderItem> orderItems = orderItemDao.selectByExample(query);
		List<OrderVo> orderVoList = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			OrderVo orderVo = new OrderVo();
			orderVo.setNum(orderItem.getNum());
			orderVo.setPrice(orderItem.getPrice());
			orderVo.setTotalFee(orderItem.getTotalFee());

			orderVo.setGoodsName(goodsDao.selectByPrimaryKey(orderItem.getGoodsId()).getGoodsName());

			Order order = orderDao.selectByPrimaryKey(orderItem.getOrderId());
			orderVo.setOrderId(orderItem.getOrderId());
			orderVo.setOrderId(orderItem.getOrderId());
			orderVo.setSourceType(order.getSourceType());
			orderVo.setStatus(order.getStatus());
			orderVo.setCreateTime(order.getCreateTime());

			orderVoList.add(orderVo);
		}
		return orderVoList;
	}

	@Override
	public PageResult findPage(Integer pageNo, Integer pageSize,String name) {
		//Mybatis分页插件
		PageHelper.startPage(pageNo,pageSize);

		OrderQuery query = new OrderQuery();
		query.createCriteria().andSellerIdEqualTo(name);
		Page<Order> page = (Page<Order>) orderDao.selectByExample(query);
		List<Order> orderList = page.getResult();
		List<OrderVo> oldOrderVoList = findAll(name);
		List<OrderVo> newOrderVoList = new ArrayList<>();
		if (orderList!=null&&orderList.size()>0){
			for (Order order : orderList) {
				for (OrderVo orderVo : oldOrderVoList) {
					if (orderVo.getOrderId().equals(order.getOrderId())){
						newOrderVoList.add(orderVo);
					}
				}
			}
		}
		return new PageResult(page.getTotal(),newOrderVoList);

	}

	@Override
	public PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name) {
		return null;
	}

	@Override
	public PageResult search(Integer page, Integer rows, String name, Order order,String searchDate) {
		//Mybatis分页插件
		PageHelper.startPage(page,rows);

		OrderQuery query = new OrderQuery();
		OrderQuery.Criteria criteria = query.createCriteria();
		criteria.andSellerIdEqualTo(name);
		//条件查询之状态查询
		if (order.getStatus()!=null){
			criteria.andStatusEqualTo(order.getStatus());
		}
		//条件查询之时间段查询
		if ("1".equals(searchDate)){
			//TODO 时间工具类调用
		}else if ("2".equals(searchDate)){
			//TODO 时间工具类调用
		}else if ("3".equals(searchDate)){
			//TODO 时间工具类调用
		}

		Page<Order> orderPage = (Page<Order>) orderDao.selectByExample(query);
		List<Order> orderList = orderPage.getResult();
		List<OrderVo> oldOrderVoList = findAll(name);
		List<OrderVo> newOrderVoList = new ArrayList<>();
		if (orderList!=null&&orderList.size()>0){
			for (Order order1 : orderList) {
				for (OrderVo orderVo : oldOrderVoList) {
					if (orderVo.getOrderId().equals(order1.getOrderId())){
						newOrderVoList.add(orderVo);
					}
				}
			}
		}
		return new PageResult(orderPage.getTotal(),newOrderVoList);
	}
}
