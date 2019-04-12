package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.FastArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import vo.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单管理
 */
@Service
@Transactional
public class OrderServiceImpl implements  OrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayLogDao payLogDao;

    @Override
    public void add(Order order) {

        //缓存中购物车
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(order.getUserId());


        //日志表  支付金额
        long total = 0;
        //订单集合
        List<Long> ids = new ArrayList<>();


        for (Cart cart : cartList) {
            //保存订单

            //订单ID  分布式ID
            long id = idWorker.nextId();
            ids.add(id);
            order.setOrderId(id);
            //金额
            double totalPrice = 0;

            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //库存ID
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());

                //数量
                //订单详情ID
                orderItem.setId(idWorker.nextId());
                //商品id
                orderItem.setGoodsId(item.getGoodsId());
                //订单ID 外键
                orderItem.setOrderId(id);
                //标题
                orderItem.setTitle(item.getTitle());
                //价格
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(item.getPrice().multiply(new BigDecimal(orderItem.getNum())));

                totalPrice += orderItem.getTotalFee().doubleValue();

                //图片路径
                orderItem.setPicPath(item.getImage());
                //商家ID
                orderItem.setSellerId(item.getSellerId());

                //保存订单详情表
                orderItemDao.insertSelective(orderItem);

            }
            //给订单表设置金额
            order.setPayment(new BigDecimal(totalPrice));

            total += order.getPayment().longValue();

            //订单状态
            order.setStatus("1");
            //时间
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());

            //订单来源
            order.setSourceType("2");
            //商家ID
            order.setSellerId(cart.getSellerId());
            //保存订单
            orderDao.insertSelective(order);

        }

        //保存日志表
        PayLog payLog = new PayLog();
        //ID 支付订单ID
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));

        //时间
        payLog.setCreateTime(new Date());
        //付款时间
        //总金额 分
        payLog.setTotalFee(total*100);

        //用户id
        payLog.setUserId(order.getUserId());


        //支付状态
        payLog.setTradeState("0");


        //订单集合 [123,456,6787]
        payLog.setOrderList(ids.toString().replace("[","").replace("]",""));

        //付款方式
        payLog.setPayType("1");

        //保存完成
        payLogDao.insertSelective(payLog);

        //保存缓存一份
        redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);

        //清空
        //redisTemplate.boundHashOps("CART").delete(order.getUserId());

    }
}
