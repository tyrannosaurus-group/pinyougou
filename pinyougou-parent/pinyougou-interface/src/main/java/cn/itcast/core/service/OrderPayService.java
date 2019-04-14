package cn.itcast.core.service;

import entity.PageResult;

public interface OrderPayService {
    PageResult findPage(String name, Integer page, Integer rows);
}
