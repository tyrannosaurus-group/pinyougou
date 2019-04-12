package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.log.PayLogQuery;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.FastArrayList;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import vo.Cart;
import vo.OrderVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public PageResult findOrderList(Integer pageNum, Integer pageSize,String name) {
        PageHelper.startPage(pageNum,pageSize);
        Page<OrderVo> page = null;
        //声明orderVo,准备组装
        OrderVo orderVo = null;
        //根究用户名查询日志表
        PayLogQuery payLogQuery = new PayLogQuery();
        payLogQuery.createCriteria().andUserIdEqualTo(name);
        List<PayLog> payLogList = payLogDao.selectByExample(payLogQuery);

        ArrayList<OrderItem> orderitems = new ArrayList<>();
        for (PayLog payLog : payLogList) {
            //根据日志表的订单id集合查询order对象
            //设置下单时间
            orderVo.setOrderTime(payLog.getCreateTime());
            //设置实际付款
            orderVo.setPayment(payLog.getTotalFee());
            //设置订单id(日志表)
            orderVo.setOrderId(Long.parseLong(payLog.getOutTradeNo()));
            //付款状态

            //商品详情集合
            String orderIds = payLog.getOrderList();
            List<Long> orderIdList = turnToList(orderIds);
            for (Long orderId : orderIdList) {
                OrderItemQuery itemQuery = new OrderItemQuery();
                itemQuery.createCriteria().andOrderIdEqualTo(orderId);
                OrderItem orderItem = (OrderItem) orderItemDao.selectByExample(itemQuery);
                orderitems.add(orderItem);
            }

        }

        return new PageResult(page.getTotal(),page.getResult());
    }
    //订单id字符串转换为Long类型集合
    public List<Long> turnToList(String str){
        String[] strs = str.split(",");
        List<String> list = Arrays.asList(strs);
        List<Long> longs = new ArrayList<>();
        for (String s : list) {
            long parseLong = Long.parseLong(s);
            longs.add(parseLong);
        }
        return longs;
    }
}
