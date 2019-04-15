package vo;

import java.io.Serializable;

public class UserAnalyzeVo implements Serializable {
    private int num;

    private int active;

    private int sleep;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
}
