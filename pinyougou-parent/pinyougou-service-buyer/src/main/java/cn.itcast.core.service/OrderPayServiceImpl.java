package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vo.Cart;
import vo.OrderVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderPayServiceImpl implements OrderPayService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PayLogDao payLogDao;
    @Override
    public PageResult findPage(String name, Integer page, Integer rows) {
        //Mybatis分页插件
//        PageHelper.startPage(page, rows);
        List<OrderVo> orderVoList = findOrderVoList(name);
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orderVoList);
        if (orderVoList.size()>=page*rows){
            pageInfo.setList(orderVoList.subList((page-1)*rows,page*rows));
        }else {
            pageInfo.setList(orderVoList.subList((page-1)*rows,orderVoList.size()));
        }
//        Page<OrderVo> orderPage = (Page<OrderVo>)orderVoList;
//        return new PageResult(orderPage.getTotal(),orderPage.getResult());
        return new PageResult(Integer.toUnsignedLong(orderVoList.size()),pageInfo.getList());
    }

    @Override
    public void payMoney(Long orderItemId,String name) {
        //更改订单状态
        Order order = new Order();
        order.setOrderId(orderItemId);
        order.setStatus("2");
        orderDao.updateByPrimaryKeySelective(order);
        //写入付款日志
        IdWorker idWorker = new IdWorker();
        long payLogId = idWorker.nextId();
        PayLog payLog = new PayLog();
        payLog.setOutTradeNo(String.valueOf(payLogId));
        payLog.setPayTime(new Date());
        payLog.setPayType("1");
        payLog.setUserId(name);
        payLog.setTradeState("1");
        payLog.setOrderList(String.valueOf(orderItemId));
        payLogDao.insertSelective(payLog);
    }

    @Override
    public void updateStatus(Long[] ids, String name) {
        if (null!=ids&&ids.length>0){
            for (Long id : ids) {
                Order order = new Order();
                order.setOrderId(id);
                order.setStatus("2");
                orderDao.updateByPrimaryKeySelective(order);
            }
            //写入付款日志
            IdWorker idWorker = new IdWorker();
            long payLogId = idWorker.nextId();
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(payLogId));
            payLog.setPayTime(new Date());
            payLog.setPayType("1");
            payLog.setUserId(name);
            payLog.setTradeState("1");
            payLog.setOrderList(String.valueOf(ids.toString().replace("[","").replace("]","")));
            payLogDao.insertSelective(payLog);
        }
    }

    @Override
    public void delPay(Long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("6");
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public Order findOrderById(String orderId) {
        return  orderDao.selectByPrimaryKey(Long.parseLong(orderId));

    }

    List<OrderVo> findOrderVoList(String name){
        ArrayList<OrderVo> orderVoList = new ArrayList<>();
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andUserIdEqualTo(name).andStatusEqualTo("1");
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        for (Order order : orderList) {
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());
            List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
            for (OrderItem orderItem : orderItemList) {

                Seller seller = sellerDao.selectByPrimaryKey(orderItem.getSellerId());
                Goods goods = goodsDao.selectByPrimaryKey(orderItem.getGoodsId());
                //新建orderVo对象并装满
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderId(String.valueOf(order.getOrderId()));
                orderVo.setOrderItemId(orderItem.getItemId());
                orderVo.setBigPrice(goods.getPrice());
                orderVo.setPrice(orderItem.getPrice().doubleValue());
                orderVo.setNum(orderItem.getNum());
                orderVo.setTotalFee(orderItem.getTotalFee());
                orderVo.setSourceType(order.getSourceType());
                orderVo.setCreateTime(order.getCreateTime());
                orderVo.setCreateTime(order.getCreateTime());
                orderVo.setStatus(order.getStatus());
//                orderVo.setSpec(goods.g);
                orderVo.setNickName(seller.getNickName());
                orderVo.setTitle(orderItem.getTitle());
//                orderVo.setOrderTime(order.getCloseTime());
                orderVo.setPicPath(orderItem.getPicPath());
                orderVo.setPayment(orderItem.getTotalFee().doubleValue());
                orderVoList.add(orderVo);
            }
        }
        return orderVoList;
    }

}
