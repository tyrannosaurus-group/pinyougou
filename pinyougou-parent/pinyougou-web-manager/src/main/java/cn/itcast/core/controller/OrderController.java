package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.FindOrderService;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderCountVo;
import vo.OrderVo;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private FindOrderService findOrderService;

    //提交订单
    @RequestMapping("/search")
    public PageResult search(Integer pageNo, Integer pageSize, @RequestBody OrderVo orderVo) {
        PageResult search = findOrderService.search(pageNo, pageSize,orderVo);
        return search;
    }

    @RequestMapping("/getdata")
    public String getdata(){
        //模拟日期   周一到周日
        //需要的数据   data1  每天的订单数量  放到一个集合中
        //            data2  每天订单所产生的总金额
         //获取data1  订单数量

        HashMap<Object, Object> map = new HashMap<>();

        List<Integer>PriceList = new ArrayList<>();
        List<Integer>NumList = new ArrayList<>();
        Map<String,List<Order>>orders = findOrderService.findOrders();
        //System.out.println(orders);
        if (orders!=null&&orders.size()>0){
            Set<String> keySet = orders.keySet();
              if (keySet!=null&&keySet.size()>0){
                  for (String s : keySet) {
                      List<Order> orders1 = orders.get(s);
                          if (orders1!=null&&orders1.size()>0){
                              Integer count = 0;


                              for (Order order : orders1) {
                                  int i = order.getPayment().intValue();
                                  count +=i;
                              };
                              NumList.add(orders1.size());
                              PriceList.add(count);
                          }
                  }
              }


        };

         map.put("num",NumList);
         map.put("price",PriceList);

         return  JSON.toJSONString(map);















//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(120);
//        list.add(210);
//        list.add(445);
//        list.add(656);
//        list.add(367);
//        list.add(478);
//        list.add(589);
//        String s = JSON.toJSONString(list);
//        return s;
    }

    @RequestMapping("/orderCount")
    public OrderCountVo orderCount() {
        return findOrderService.orderCount();
    }
}
