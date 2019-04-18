package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Cart;

import java.util.List;

/**
 * 收藏管理
 */
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Reference
    private CollectService collectService;
    @Reference
    private CartService cartService;

    @RequestMapping("/removeGoodsToCollect")
    public Result removeGoodsToCollect(Long itemId) {
        try {
            //从购物车列表移除这个商品
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Cart> cartList = cartService.findCartListFromRedis(name);
            for (Cart cart : cartList) {
                List<OrderItem> orderItemList = cart.getOrderItemList();
                for (OrderItem orderItem : orderItemList) {
                    if (itemId == orderItem.getItemId()) {
                        orderItemList.remove(orderItem);
                        //往收藏列表添加这个商品
                        collectService.addOrderItemToRedis(orderItem,name);
                    }
                }
            }
            cartService.addCartListToRedis(cartList, name);
            return new Result(true, "收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "收藏失败");
        }
    }
}
