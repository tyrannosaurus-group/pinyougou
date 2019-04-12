package cn.itcast.core.controller;

import cn.itcast.core.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付管理
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    //获取金额 订单号 code_url
    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(name);
    }

    //查询订单是否已经付款
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) throws Exception {


        int x = 0;
        //无限循环
        while (true) {
            Map<String, String> map = payService.queryPayStatus(out_trade_no);

            //判断是否付款
            if ("NOTPAY".equals(map.get("trade_state"))) {
                //未付款

                Thread.sleep(3000);

                x++;
                if(x > 100){
                    //5分钟 视为放弃

                    //调用Api 将 此二维码失效
                    //TODO 调用关闭订单Api

                    return new Result(false,"支付超时");
                }
            }else{
                //视认为 支付成功
                return new Result(true,"支付成功");
            }

        }

    }
}
