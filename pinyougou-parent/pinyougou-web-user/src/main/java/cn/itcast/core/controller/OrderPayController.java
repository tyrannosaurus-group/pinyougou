package cn.itcast.core.controller;

import cn.itcast.core.service.OrderPayService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Cart;
import vo.OrderVo;

import java.util.List;

/**
 * 未付款订单管理
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("orderPay")
public class OrderPayController {

    @Reference
    private OrderPayService orderPayService;
    @RequestMapping("findPage")
    public PageResult findPage(Integer page,Integer rows){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderPayService.findPage(name,page,rows);
    }
    @RequestMapping("payMoney")
    public Result payMoney(Long orderItemId){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            orderPayService.payMoney(orderItemId,name);
            return new Result(true,"付款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"付款失败");
        }
    }
    @RequestMapping("updateStatus")
    public Result updateStatus(Long[] ids){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            orderPayService.updateStatus(ids,name);
            return new Result(true,"付款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"付款失败");
        }
    }
    @RequestMapping("delPay")
    public Result delPay(Long orderId){
        try {
            orderPayService.delPay(orderId);
            return new Result(true,"付款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"付款失败");
        }
    }
}
