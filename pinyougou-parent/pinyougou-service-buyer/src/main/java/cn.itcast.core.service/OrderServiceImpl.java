package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.log.PayLogQuery;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import cn.itcast.core.pojo.specification.Specification;
import com.alibaba.dubbo.config.annotation.Service;
import entity.PageResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctc.wstx.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import vo.Cart;
import vo.OrderVo;
import vo.PageBean;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单管理
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Override
    public List<OrderItem> findAll() {
        return null;
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize, String name) {
        return null;
    }

    @Override
    public PageResult search(Integer page, Integer rows, String name, Order order, String searchDate) {
        return null;
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        return null;
    }

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
    public PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name) {
        PageBean<OrderVo> pageBean = new PageBean<>();
        pageBean.setPageNumber(pageNum);
        pageBean.setPageSize(pageSize);

        //声明一个集合,准备装ordervo,一装满,就装上
        ArrayList<OrderVo> list = new ArrayList<>();

        //根据用户名查询日志表
        PayLogQuery payLogQuery = new PayLogQuery();
        payLogQuery.createCriteria().andUserIdEqualTo(name);
        List<PayLog> payLogList = payLogDao.selectByExample(payLogQuery);

        //现在orderVo是空的,老子要装满,仅此而已,商品详情
        if (payLogList != null && payLogList.size() > 0) {
            for (PayLog payLog : payLogList) {
                //商品详情集合 这就找到商品详情id的集合了,遍历就能找到商品详情
                String orderIds = payLog.getOrderList();
                List<Long> orderIdList = turnToList(orderIds);
                if (orderIdList != null && orderIdList.size() > 0) {
                    for (Long orderId : orderIdList) {
                        //声明orderVo,准备组装
                        OrderVo BeforeOrderVo = new OrderVo();
                        //先在外层的循环,找一个order,毕竟是唯一的
                        Order order = orderDao.selectByPrimaryKey(orderId);
                        //先把order里面的属性加给ordervo
                        BeforeOrderVo.setOrderId(String.valueOf(orderId));
                        BeforeOrderVo.setNickName(order.getSellerId());
                        BeforeOrderVo.setChliOrderTime(order.getCreateTime());
                        BeforeOrderVo.setPayment(order.getPayment().doubleValue());
                        //订单状态,现在就付款和没有付款两种,还没有涉及到物流
                        BeforeOrderVo.setStatus(order.getStatus());
                        //以上,order已经竭尽所能了,接下来,商品详情
                        OrderItemQuery orderItemQuery = new OrderItemQuery();
                        orderItemQuery.createCriteria().andOrderIdEqualTo(orderId);
                        //到这,商品详情的集合就找到了,宝藏在里面,遍历
                        List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                        if (orderItemList != null && orderItemList.size() > 0)
                            for (OrderItem orderItem : orderItemList) {
                                OrderVo vo = new OrderVo();
                                //把BeforeOrderVo的属性设置到vo中,说实话,这么写实在是很low
                                vo.setOrderId(BeforeOrderVo.getOrderId());
                                vo.setNickName(BeforeOrderVo.getNickName());
                                vo.setChliOrderTime(BeforeOrderVo.getChliOrderTime());
                                vo.setPayment(BeforeOrderVo.getPayment());
                                vo.setStatus(BeforeOrderVo.getStatus());
                                //欸嘿嘿,找到一个商品详情,耍耍
                                vo.setTitle(orderItem.getTitle());
                                vo.setPicPath(orderItem.getPicPath());
                                vo.setPrice(new BigDecimal(orderItem.getPrice().doubleValue()));
                                vo.setNum(orderItem.getNum());
                                //下面是规格了,爽爽
                                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                                String spec = item.getSpec();
                                Map specMap = JSON.parseObject(spec, Map.class);
//                                String[] specs = (String[]) specMap.values().toArray();
                                if (specMap!=null){
                                    Object[] objects = specMap.values().toArray();
                                    String s = StringUtils.join(objects, ",");
                                    String[] split = s.split(",");
                                    ArrayList<String> strs= new ArrayList<>();
                                    for (String s1 : split) {
                                        strs.add(s1);
                                    }
                                    vo.setSpec(strs);
                                    //到这里,一个ordervo就装满了,全部循环遍历完事,基本上就是一堆了,需要集合,然后就来了
                                    list.add(vo);
                                }
                            }
                    }
                }
            }
        }
        //设置总条数
        int size = list.size();
        pageBean.setTotal(size);
        //设置分页结果集
        List<OrderVo> pageList = new ArrayList<>();
        pageList.clear();
//        pageList = null;
        for (int i = (pageNum - 1) * pageSize; i < pageNum * pageSize; i++) {
            if (i < size) {
                pageList.add(list.get(i));
            }else{
                break;
            }
        }
//        OrderVo noVo = new OrderVo();
//        pageList.add(0,noVo);
        //装车基本上结束了,要往分页对象里面放,不知道能不能放进去,硬塞,行,报错了,说是不能转
        //明天不用分页软件,手动分页试试.
        pageBean.setData(pageList);
        //也不知道这个位置加上缓存有没有用
        redisTemplate.boundHashOps("OrderVoList").put(name, pageBean);
        return pageBean;
    }

    //订单id字符串转换为Long类型集合
    public List<Long> turnToList(String str) {
        String[] strs = str.split(",");
        List<Long> longs = new ArrayList<>();
        for (String s : strs) {
            String trim = s.trim();
            long parseLong = Long.parseLong(trim);
            longs.add(parseLong);
        }
        return longs;
    }

    public static void main(String[] args) {
        String str = "111,222";
        String[] strs = str.split(",");
        List<Long> longs = new ArrayList<>();
        for (String s : strs) {
            long parseLong = Long.parseLong(s);
            System.out.println(parseLong);
            longs.add(parseLong);
        }
    }

    @Override
    public List<OrderVo> findAll(String name) {
        return null;
    }

    /*@Override
    public PageResult search(Integer page, Integer rows, String name, Order order, String searchDate) {
        return null;
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, String name) {
        return null;
    }

    @Override
    public PageBean<OrderVo> findOrderList(Integer pageNum, Integer pageSize, String name) {
        return null;
    }*/
}
