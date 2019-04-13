package vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderVo implements Serializable{

	//订单Id
	private Long orderId;          //order
	//商品名称
	private String goodsName;  //orderitem中有goodsID
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
