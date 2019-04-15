package cn.itcast.core.service;

import cn.itcast.core.pojo.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class CollectServiceImpl implements CollectService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addOrderItemToRedis(OrderItem orderItem, String name) {
        List<OrderItem> orderItemList = (List<OrderItem>) redisTemplate.boundHashOps("COLLECT").get(name);
        if (orderItemList == null) {
            List<OrderItem> orderItemList1 = new ArrayList<>();
            orderItemList1.add(orderItem);
            redisTemplate.boundHashOps("COLLECT").put(name,orderItemList1);
        }
        orderItemList.add(orderItem);
        redisTemplate.boundHashOps("COLLECT").put(name,orderItemList);
    }

    @Override
    public List<OrderItem> findList(String name) {
        List<OrderItem> collectList = (List<OrderItem>) redisTemplate.boundHashOps("COLLECT").get(name);
        return collectList;
    }}
