package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;

import java.util.List;

public interface CollectService {
    List<OrderItem> findList(String name);
}
