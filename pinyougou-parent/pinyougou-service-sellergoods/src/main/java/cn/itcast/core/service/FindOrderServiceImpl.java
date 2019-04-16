package cn.itcast.core.service;


import cn.itcast.common.utils.DateUtils;
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
import org.springframework.transaction.annotation.Transactional;
import vo.OrderCountVo;
import vo.OrderVo;
import vo.UserOrderVo;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单管理
 */
@Service
@Transactional
public class FindOrderServiceImpl implements FindOrderService {


    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public PageResult search(Integer pageNo, Integer pageSize, OrderVo orderVo) {

        PageHelper.startPage(pageNo, pageSize);

        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();

        if (null != orderVo.getStatus() && !"".equals(orderVo.getStatus())) {
            criteria.andStatusEqualTo(orderVo.getStatus());
        }


        List<Order> orderList = orderDao.selectByExample(orderQuery);


        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        List<Order> list = pageInfo.getList();
        List<OrderVo> orderVoList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (Order order : list) {

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());

                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);

                if (null != orderItems && orderItems.size() > 0) {
                    for (OrderItem orderItem : orderItems) {

                        OrderVo orderVo1 = new OrderVo();

                        orderVo1.setId(order.getOrderId());
                        orderVo1.setStatus(order.getStatus());
                        orderVo1.setSourceType(order.getSourceType());
                        orderVo1.setCreateTime(order.getCreateTime());

                        orderVo1.setGoodsName(orderItem.getTitle());
                        orderVo1.setPrice(orderItem.getPrice());
                        orderVo1.setNum(orderItem.getNum());
                        orderVo1.setTotalFee(orderItem.getTotalFee());

                        orderVoList.add(orderVo1);
                    }
                }
            }
        }

        return new PageResult(pageInfo.getTotal(), orderVoList);
    }

    @Override
    public Map<String, List<Order>> findOrders() {
        HashMap<String, List<Order>> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        Date date5 = null;
        Date date6 = null;
        Date date7 = null;
        Date date8 = null;
        try {
            date1 = sdf.parse("2017-08-24 00:00:00");
            date2 = sdf.parse("2017-08-25 00:00:00");
            date3 = sdf.parse("2017-08-25 24:00:00");
            date4 = sdf.parse("2017-08-26 24:00:00");
            date5 = sdf.parse("2017-10-12 24:00:00");
            date6 = sdf.parse("2017-10-14 24:00:00");
            date7 = sdf.parse("2018-03-08 24:00:03");
            date8 = sdf.parse("2019-08-24 20:44:03");
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderQuery orderQuery1 = new OrderQuery();
        orderQuery1.createCriteria().andCreateTimeBetween(date1, date2);
        List<Order> orders1 = orderDao.selectByExample(orderQuery1);
        map.put("1", orders1);

        OrderQuery orderQuery2 = new OrderQuery();
        orderQuery2.createCriteria().andCreateTimeBetween(date2, date3);
        List<Order> orders2 = orderDao.selectByExample(orderQuery2);
        map.put("2", orders2);


        OrderQuery orderQuery3 = new OrderQuery();
        orderQuery3.createCriteria().andCreateTimeBetween(date3, date4);
        List<Order> orders3 = orderDao.selectByExample(orderQuery3);
        map.put("3", orders3);


        OrderQuery orderQuery4 = new OrderQuery();
        orderQuery4.createCriteria().andCreateTimeBetween(date4, date5);
        List<Order> orders4 = orderDao.selectByExample(orderQuery4);
        map.put("4", orders4);


        OrderQuery orderQuery5 = new OrderQuery();
        orderQuery5.createCriteria().andCreateTimeBetween(date5, date6);
        List<Order> orders5 = orderDao.selectByExample(orderQuery5);
        map.put("5", orders5);


        OrderQuery orderQuery6 = new OrderQuery();
        orderQuery6.createCriteria().andCreateTimeBetween(date6, date7);
        List<Order> orders6 = orderDao.selectByExample(orderQuery6);
        map.put("6", orders6);


        OrderQuery orderQuery7 = new OrderQuery();
        orderQuery7.createCriteria().andCreateTimeBetween(date7, date8);
        List<Order> orders7 = orderDao.selectByExample(orderQuery7);
        map.put("7", orders7);
        return map;
    }

    @Override
    public OrderCountVo orderCount() {
        OrderCountVo orderCountVo = new OrderCountVo();

        orderCountVo.setCount(orderDao.countByExample(null));

        List<Order> orderList = orderDao.selectByExample(null);
        double total = 0;
        for (Order order : orderList) {
            total += order.getPayment().doubleValue();
        }

        orderCountVo.setTotal(new BigDecimal(total));

        OrderQuery orderQuery = new OrderQuery();
        String[] dayStartAndEndTimePointStr = DateUtils.getDayStartAndEndTimePointStr(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(dayStartAndEndTimePointStr[0]);
            date2 = simpleDateFormat.parse(dayStartAndEndTimePointStr[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderQuery.createCriteria().andCreateTimeBetween(date1, date2);

        orderCountVo.setToday(orderDao.countByExample(orderQuery));

        List<Order> orderList1 = orderDao.selectByExample(orderQuery);
        double todayFee = 0;
        for (Order order : orderList1) {
            todayFee += order.getPayment().doubleValue();
        }
        orderCountVo.setTodayFee(new BigDecimal(todayFee));

        return orderCountVo;
    }

    @Override
    public PageResult download(Integer pageNo, Integer pageSize,UserOrderVo vo) {



        List<Order> orderList= orderDao.selectByExample(null);

        HashSet<String> hashSet = new HashSet<>();
        for (Order order : orderList) {
            hashSet.add(order.getUserId());
        }

        List<UserOrderVo> list = new ArrayList<>();;
        Page<Order> page=null;

        for (String s : hashSet) {


            UserOrderVo userOrderVo = new UserOrderVo();

            userOrderVo.setUserId(s);

            OrderQuery orderQuery = new OrderQuery();
            OrderQuery.Criteria criteria = orderQuery.createCriteria();
            criteria.andUserIdEqualTo(s);

            if (null != vo.getStartTime() && !"".equals(vo.getStartTime())&&null != vo.getEndTime() && !"".equals(vo.getEndTime())) {
                criteria.andCreateTimeBetween(vo.getStartTime(), vo.getEndTime());
            }

            if (null != vo.getUserId() && !"".equals(vo.getUserId())) {
                criteria.andUserIdEqualTo("%"+vo.getUserId()+"%");
            }

            PageHelper.startPage(pageNo, pageSize);

            page = (Page<Order>) orderDao.selectByExample(orderQuery);
            List<Order> orderList1 = page.getResult();

            for (Order order : orderList1) {

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());

                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);

                order.setOrderItemList(orderItems);

            }
            userOrderVo.setOrderList(orderList1);

            list.add(userOrderVo);
        }


        return new PageResult(page.getTotal(),list);
    }

    @Override
    public List<UserOrderVo> findAllUserOrders() {

        List<Order> orderList= orderDao.selectByExample(null);

        HashSet<String> hashSet = new HashSet<>();
        for (Order order : orderList) {
            hashSet.add(order.getUserId());
        }

        List<UserOrderVo> list = new ArrayList<>();;
        Page<Order> page=null;

        for (String s : hashSet) {


            UserOrderVo userOrderVo = new UserOrderVo();

            userOrderVo.setUserId(s);

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.createCriteria().andUserIdEqualTo(s);



            List<Order> orderList1 =orderDao.selectByExample(orderQuery);

            for (Order order : orderList1) {

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());

                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);

                order.setOrderItemList(orderItems);

            }
            userOrderVo.setOrderList(orderList1);

            list.add(userOrderVo);
        }
        return list;
    }


}
