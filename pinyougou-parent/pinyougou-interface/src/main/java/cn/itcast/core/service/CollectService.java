package cn.itcast.core.service;

import cn.itcast.core.pojo.order.OrderItem;

import java.util.List;

public interface CollectService {
    void addOrderItemToRedis(OrderItem orderItem, String name);

    List<OrderItem> findList(String name);
}
