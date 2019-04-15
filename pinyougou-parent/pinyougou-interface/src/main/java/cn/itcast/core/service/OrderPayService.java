package cn.itcast.core.service;

import entity.PageResult;

public interface OrderPayService {
    PageResult findPage(String name, Integer page, Integer rows);

    void payMoney(Long orderItemId,String name);

    void updateStatus(Long[] ids, String name);

    void delPay(Long orderId);
}
