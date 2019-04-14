package vo;

import cn.itcast.core.pojo.address.Addrnow;
import cn.itcast.core.pojo.user.User;

import java.io.Serializable;

public class UserVo implements Serializable {
    private User user;
    private Addrnow addrnow;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Addrnow getAddrnow() {
        return addrnow;
    }

    public void setAddrnow(Addrnow addrnow) {
        this.addrnow = addrnow;
    }
}
