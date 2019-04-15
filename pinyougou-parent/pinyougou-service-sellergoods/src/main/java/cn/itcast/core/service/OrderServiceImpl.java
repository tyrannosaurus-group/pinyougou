package cn.itcast.core.service;

import cn.itcast.common.utils.DateUtils;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
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
import vo.OrderStatistics;
import vo.OrderVo;
import vo.PageBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	public List<OrderItem> findAll() {
		return null;
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
			orderVo.setSourceType(order.getSourceType());
			orderVo.setStatus(order.getStatus());
			orderVo.setCreateTime(order.getCreateTime());

			orderVoList.add(orderVo);
		}
		return orderVoList;
	}

	@Override
	public PageResult findPage(Integer pageNo, Integer pageSize, String name) {
		//Mybatis分页插件
		PageHelper.startPage(pageNo, pageSize);

		OrderQuery query = new OrderQuery();
		query.createCriteria().andSellerIdEqualTo(name);
		Page<Order> page = (Page<Order>) orderDao.selectByExample(query);
		List<Order> orderList = page.getResult();
		List<OrderVo> oldOrderVoList = findAll(name);
		List<OrderVo> newOrderVoList = new ArrayList<>();
		if (orderList != null && orderList.size() > 0) {
			for (Order order : orderList) {
				for (OrderVo orderVo : oldOrderVoList) {
					if (orderVo.getOrderId().equals(order.getOrderId())) {
						newOrderVoList.add(orderVo);
					}
				}
			}
		}
		return new PageResult(page.getTotal(), newOrderVoList);

	}

	@Override
	public PageResult search(Integer page, Integer rows, String name, Order order, String searchDate) {
		//Mybatis分页插件
		PageHelper.startPage(page, rows);

		OrderQuery query = new OrderQuery();
		OrderQuery.Criteria criteria = query.createCriteria();
		criteria.andSellerIdEqualTo(name);
		//条件查询之状态查询
		if (order.getStatus() != null) {
			criteria.andStatusEqualTo(order.getStatus());
		}
		//条件查询之时间段查询

		if ("1".equals(searchDate)) {
			//日订单
			//Order [createTime=Thu Apr 18 00:00:00 CST 2019]
			String[] dayStr = DateUtils.getDayStartAndEndTimePointStr(order.getCreateTime());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date[] datexx = new Date[2];
			try {
				datexx[0] = simpleDateFormat.parse(dayStr[0]);
				datexx[1] = simpleDateFormat.parse(dayStr[1]);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			criteria.andCreateTimeBetween(datexx[0], datexx[1]);
		} else if ("2".equals(searchDate)) {
			//周订单
			Date[] weekDate = DateUtils.getWeekStartAndEndDate(order.getCreateTime());
			criteria.andCreateTimeBetween(weekDate[0], weekDate[1]);
		} else if ("3".equals(searchDate)) {
			//月订单
			Date[] monthDate = DateUtils.getMonthStartAndEndDate(order.getCreateTime());
			criteria.andCreateTimeBetween(monthDate[0], monthDate[1]);
		}

		Page<Order> orderPage = (Page<Order>) orderDao.selectByExample(query);
		List<Order> orderList = orderPage.getResult();
		List<OrderVo> oldOrderVoList = findAll(name);
		List<OrderVo> newOrderVoList = new ArrayList<>();
		if (orderList != null && orderList.size() > 0) {
			for (Order order1 : orderList) {
				for (OrderVo orderVo : oldOrderVoList) {
					if (orderVo.getOrderId().equals(order1.getOrderId())) {
						newOrderVoList.add(orderVo);
					}
				}
			}
		}
		return new PageResult(orderPage.getTotal(), newOrderVoList);
	}

	@Override
	public PageResult findPage(Integer pageNo, Integer pageSize) {
		return null;
	}

	@Override
	public PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name) {
		return null;
	}

	@Override
	public PageResult searchSta(Integer page, Integer rows, String name, Order order, String searchDate) {


		OrderQuery query = new OrderQuery();
		OrderQuery.Criteria criteria = query.createCriteria();
		criteria.andSellerIdEqualTo(name);
		if ("1".equals(searchDate)) {
			//日订单
			//Order [createTime=Thu Apr 18 00:00:00 CST 2019]
			String[] dayStr = DateUtils.getDayStartAndEndTimePointStr(order.getCreateTime());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date[] datexx = new Date[2];
			try {
				datexx[0] = simpleDateFormat.parse(dayStr[0]);
				datexx[1] = simpleDateFormat.parse(dayStr[1]);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			criteria.andCreateTimeBetween(datexx[0], datexx[1]);
		} else if ("2".equals(searchDate)) {
			//周订单
			Date[] weekDate = DateUtils.getWeekStartAndEndDate(order.getCreateTime());
			criteria.andCreateTimeBetween(weekDate[0], weekDate[1]);
		} else if ("3".equals(searchDate)) {
			//月订单
			Date[] monthDate = DateUtils.getMonthStartAndEndDate(order.getCreateTime());
			criteria.andCreateTimeBetween(monthDate[0], monthDate[1]);
		}

		List<Order> orderList = orderDao.selectByExample(query);

		List<Long> ids = new ArrayList<>();
		for (Order order1 : orderList) {
			ids.add(order1.getOrderId());
		}
		OrderItemQuery orderItemQuery = new OrderItemQuery();
		orderItemQuery.createCriteria().andOrderIdIn(ids);

		List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
		Set<Long> goodsIds = new HashSet<>();
		for (OrderItem orderItem : orderItemList) {
			goodsIds.add(orderItem.getGoodsId());
		}

		List<Long> orderItemIds = new ArrayList<>();
		for (OrderItem orderItem : orderItemList) {
			orderItemIds.add(orderItem.getId());
		}
		List<OrderStatistics> orderStatistics = orderItemDao.selectGroupByGoodsId(orderItemIds);

		//Mybatis分页插件
		PageHelper.startPage(page, rows);
		GoodsQuery goodsQuery = new GoodsQuery();
		goodsQuery.createCriteria().andIdIn(new ArrayList<>(goodsIds));
		Page<Goods> pages = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
		List<Goods> goodsList = pages.getResult();
		List<OrderStatistics> newOrderStatisticsList = new ArrayList<>();
		if (goodsList != null && goodsList.size() > 0) {
			for (Goods goods : goodsList) {
				for (OrderStatistics orderStatistic : orderStatistics) {
					if (goods.getId().equals(orderStatistic.getGoodsId())) {
						orderStatistic.setGoodsName(goods.getGoodsName());
						orderStatistic.setPrice(goods.getPrice());
						newOrderStatisticsList.add(orderStatistic);
					}
				}
			}
		}
		return new PageResult(pages.getTotal(), newOrderStatisticsList);
	}

	@Override
	public Map<String, List> zheXianTu(String name) {
		Map<String, List> map = new HashMap<>();
		List<String> listDate = new ArrayList<>();
		List<BigDecimal> listPayMent = new ArrayList<>();

		OrderQuery orderQuery = new OrderQuery();
		orderQuery.createCriteria().andSellerIdEqualTo(name);
		List<Order> orderList = orderDao.selectByExample(orderQuery);
		for (Order order : orderList) {
			Date createTime = order.getCreateTime();
			String dateToStr = DateUtils.formatDateToStr(createTime);

			int i = listDate.indexOf(dateToStr);
			if (-1 == i) {
				listDate.add(dateToStr);
				listPayMent.add(order.getPayment());
			}else {
				listPayMent.set(i,listPayMent.get(i).add(order.getPayment()));
			}
		}
		map.put("date",listDate);
		map.put("price",listPayMent);

		return map;
	}
}
