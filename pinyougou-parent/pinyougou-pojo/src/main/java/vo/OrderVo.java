package vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class OrderVo implements Serializable{

	//订单Id
	private Long orderId;          //order
	//商品名称
	private String goodsName;  //orderitem中有goodsID
	//商品价格
	private double price;      //orderItem
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
//订单id

    //商品名称

    //商品价格

    //商品数量

    //订单实付金额

    //订单来源
    /**
     * 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
     */

    //创建时间
    /**
     * 订单创建时间
     */

    //付款状态
    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */

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

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
