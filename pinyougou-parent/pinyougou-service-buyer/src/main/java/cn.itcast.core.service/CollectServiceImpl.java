package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class CollectServiceImpl implements CollectService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemDao itemDao;
    @Override
    public List<OrderItem> findList(String name) {
        List<OrderItem> CollectList = (List<OrderItem>) redisTemplate.boundHashOps("COLLECT").get(name);
        return CollectList;
    }
}
