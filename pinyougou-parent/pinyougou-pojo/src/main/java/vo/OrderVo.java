package vo;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.itcast.common.utils.LongJsonDeserializer;
import cn.itcast.common.utils.LongJsonSerializer;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class OrderVo implements Serializable{

	//订单Id
//    @JsonSerialize(using = LongJsonSerializer.class)
//    @JsonDeserialize(using = LongJsonDeserializer.class)
	private String orderId;          //order
    //
    private Long orderItemId;
	//商品名称
	private String goodsName;  //orderitem中有goodsID
    //商品原价
    private BigDecimal bigPrice;
	//商品价格
	private BigDecimal price;      //orderItem
	//商品数量
	private Integer num;			 //orderItem
	//订单实付金额
	private BigDecimal totalFee;      //orderItem
	//订单来源
	private String sourceType;       //order
	//创建时间
	private Date createTime;           //order
	//支付状态
	private String status;      //order



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
    //图片路径
    private String picPath;
    //单价
    //数量
    //实付款
    private double payment;
    //商品操作
    private Integer sendstatus;
    //交易状态


    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public BigDecimal getBigPrice() {
        return bigPrice;
    }

    public void setBigPrice(BigDecimal bigPrice) {
        this.bigPrice = bigPrice;
    }

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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
