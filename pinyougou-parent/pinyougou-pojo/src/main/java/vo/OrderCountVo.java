package vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderCountVo implements Serializable {
    private int count;

    private BigDecimal total;

    private int today;

    private BigDecimal todayFee;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public BigDecimal getTodayFee() {
        return todayFee;
    }

    public void setTodayFee(BigDecimal todayFee) {
        this.todayFee = todayFee;
    }
}
