package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import vo.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理
 */
@SuppressWarnings("all")
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemDao itemDao;

    //根据ID查询一个库存对象
    @Override
    public Item findItemById(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    //装满购物车
    @Override
    public List<Cart> findCartList(List<Cart> cartList) {
        for (Cart cart : cartList) {

            Item item = null;
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //
                item = findItemById(orderItem.getItemId());
                //图片
                orderItem.setPicPath(item.getImage());
                //标题
                orderItem.setTitle(item.getTitle());
                //单价
                orderItem.setPrice(item.getPrice());
                //数量 （有了）
                //小计
                orderItem.setTotalFee(
                        item.getPrice().multiply(new BigDecimal(orderItem.getNum())));

            }
            //商家名称
            cart.setSellerName(item.getSeller());
        }

        return cartList;
    }

    @Autowired
    private RedisTemplate redisTemplate;
    //合并购物车到到缓存中
    @Override
    public void addCartListToRedis(List<Cart> newCartList,String name) {
        //1:从缓存中获取出老的购物车集合
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
        //2:将新购物车集合 合并到老购物车集合
        oldCartList = mergeCartList(oldCartList,newCartList);

        //3:将合并后老车 保存到缓存中一份
        redisTemplate.boundHashOps("CART").put(name,oldCartList);

    }

    @Override
    public List<Cart> findCartListFromRedis(String name) {
        return (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
    }

    //将新购物车集合 合并到老购物车集合
    public List<Cart> mergeCartList(List<Cart> oldCartList,List<Cart> newCartList){

        //判断老车是否为空
        if(null != oldCartList && oldCartList.size() > 0){
            //判断新车是否为空
            if(null != newCartList && newCartList.size() > 0){
                //合并新车集合到老车集合
                for (Cart newCart : newCartList) {

                    //1)判断新购物车所属商家 在当前购物车集合中是否已经存在
                    int newIndexOf = oldCartList.indexOf(newCart);// newIndexOf == -1 不存在  >0 存在同时 newIndexOf 角标
                    if (newIndexOf != -1) {
                        //-- 存在
                        //找到相同的商家对象的购物车 获取出此商家对应的所有商品集合
                        Cart oldCart = oldCartList.get(newIndexOf);
                        List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();

                        //2)判断当前新商品在 上面的商品集合中是否已经存在
                        List<OrderItem> newOrderItemList = newCart.getOrderItemList();
                        for (OrderItem newOrderItem : newOrderItemList) {
                            int indexOf = oldOrderItemList.indexOf(newOrderItem);
                            if (indexOf != -1) {
                                //--- 存在
                                // 找出对应的相同商品 追加数量
                                OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                                oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                            } else {
                                //--- 不存在
                                //直接添加新商品即可
                                oldOrderItemList.add(newOrderItem);
                            }
                        }
                    } else {
                        //-- 不存在
                        //创建新的购物车对象 当作新购物车添加到购物车集合中
                        oldCartList.add(newCart);

                    }



                }
            }
        }else{
            return newCartList;
        }
        return oldCartList;
    }
}
