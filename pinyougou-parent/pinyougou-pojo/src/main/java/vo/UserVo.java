package vo;

import cn.itcast.core.pojo.address.Addrnow;
import cn.itcast.core.pojo.user.User;
import org.apache.activemq.command.IntegerResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class UserVo implements Serializable {
    /**
     * 头像地址
     */
    private String headPic;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别，1男，2女
     */
    private String sex;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 所在地
     */
    private String provinceid;
    private String cityid;
    private String areaid;
    /**
     * 职业
     */
    private String occupation;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
