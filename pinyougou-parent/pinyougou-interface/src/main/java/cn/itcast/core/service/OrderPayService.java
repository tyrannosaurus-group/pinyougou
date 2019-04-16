package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;
import vo.OrderVo;

public interface OrderPayService {
    PageResult findPage(String name, Integer page, Integer rows);

    void payMoney(Long orderItemId,String name);

    void updateStatus(Long[] ids, String name);

    void delPay(Long orderId);

    Order findOrderById(String orderId);
}
