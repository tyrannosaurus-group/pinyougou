package cn.itcast.core.controller;

import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家管理
 */
@RestController
@RequestMapping("/seller")
public class SellerController {


    @Reference
    private SellerService sellerService;
    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller){
        try {
            sellerService.add(seller);
            return new Result(true,"入驻成功");
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,"入驻失败");
        }
    }
}
