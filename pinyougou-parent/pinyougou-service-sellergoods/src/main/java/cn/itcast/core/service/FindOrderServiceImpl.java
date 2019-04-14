package cn.itcast.core.service;


import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

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
        if (null !=list&&list.size()>0) {
            for (Order order : list) {

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());

                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);

                if (null != orderItems && orderItems.size() > 0) {
                    for (OrderItem orderItem : orderItems) {

                        OrderVo orderVo1 = new OrderVo();

                        orderVo1.setOrderId(order.getOrderId());
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
}
