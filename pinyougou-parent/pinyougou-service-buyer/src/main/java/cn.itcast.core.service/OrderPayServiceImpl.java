package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.good.Brand;
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
import vo.Cart;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderPayServiceImpl implements OrderPayService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Override
    public PageResult findPage(String name, Integer page, Integer rows) {
        //Mybatis分页插件
        PageHelper.startPage(page, rows);
        String status="1";
//        Long total=orderDao.selectTotalByUserId(name,status);
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andUserIdEqualTo(name).andStatusEqualTo("1");
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        for (Order order : orderList) {
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());
            List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
            order.setOrderItemList(orderItemList);
        }
//        List<Cart> cartList=orderDao.selectOrderList(name);

        Page<Order> orderPage = (Page<Order>)orderList;
        return new PageResult(orderPage.getTotal(),orderPage.getResult());
    }

}
