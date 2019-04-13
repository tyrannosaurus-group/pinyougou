package cn.itcast.core.service;

import entity.PageResult;
import vo.OrderVo;

public interface FindOrderService {

    PageResult search(Integer pageNo, Integer pageSize, OrderVo orderVo);
}
