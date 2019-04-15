package vo;

import java.io.Serializable;

public class UserCountVo implements Serializable {
    //总人数
    private long total;

    //男性人数
    private long maleCount;

    //女性人数
    private long femaleCount;

    //正常人数
    private long normal;

    //冻结人数
    private long freeze;

    //一级用户人数
    private long one;

    //二级用户人数
    private long two;

    //三级用户人数
    private long three;

    //四级用户人数
    private long four;

    //五级用户人数
    private long five;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(long maleCount) {
        this.maleCount = maleCount;
    }

    public long getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(long femaleCount) {
        this.femaleCount = femaleCount;
    }

    public long getNormal() {
        return normal;
    }

    public void setNormal(long normal) {
        this.normal = normal;
    }

    public long getFreeze() {
        return freeze;
    }

    public void setFreeze(long freeze) {
        this.freeze = freeze;
    }

    public long getOne() {
        return one;
    }

    public void setOne(long one) {
        this.one = one;
    }

    public long getTwo() {
        return two;
    }

    public void setTwo(long two) {
        this.two = two;
    }

    public long getThree() {
        return three;
    }

    public void setThree(long three) {
        this.three = three;
    }

    public long getFour() {
        return four;
    }

    public void setFour(long four) {
        this.four = four;
    }

    public long getFive() {
        return five;
    }

    public void setFive(long five) {
        this.five = five;
    }
}
