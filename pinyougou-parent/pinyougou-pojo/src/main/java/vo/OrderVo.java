package vo;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderVo implements Serializable {

    //一个库存品的规格
    private List<String> spec;
    //商店名称
    private String nickName;
    //产品名称(标题)
    private String title;
    //下单日期(日志表)
    private Date orderTime;
    //子订单下单日期(订单表)
    private Date chliOrderTime;
    //订单ID
    private Long orderId;
    //图片路径
    private String picPath;
    //单价
    private double price;
    //数量
    private Integer num;
    //实付款
    private double payment;
    //商品操作
    private Integer sendstatus;
    //交易状态
    private String status;

    public List<String> getSpec() {
        return spec;
    }

    public void setSpec(List<String> spec) {
        this.spec = spec;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getChliOrderTime() {
        return chliOrderTime;
    }

    public void setChliOrderTime(Date chliOrderTime) {
        this.chliOrderTime = chliOrderTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Integer getSendstatus() {
        return sendstatus;
    }

    public void setSendstatus(Integer sendstatus) {
        this.sendstatus = sendstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
