package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地址管理
 */

@RestController
@RequestMapping("address")
public class AddressController {

    @Reference
    private AddressService addressService;
    @RequestMapping("findAddressList")
    public List<Address> findAddressList(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(name);
    }
    @RequestMapping("delete")
    public Result delete(Long id){
        try {
            addressService.delete(id);
            return new Result(true,"删除地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"删除地址失败");
        }
    }
    @RequestMapping("setDefault")
    public Result setDefault(Long id){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            addressService.setDefault(name,id);
            return new Result(true,"设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"设置失败");
        }
    }
    @RequestMapping("update")
    public Result update(@RequestBody Address tbAddress){
        try {
            addressService.update(tbAddress);
            return new Result(true,"更新地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"更新地址失败");
        }
    }
    @RequestMapping("add")
    public Result add(@RequestBody Address tbAddress){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            addressService.add(tbAddress,name);
            return new Result(true,"添加地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"添加地址失败");
        }
    }
}
