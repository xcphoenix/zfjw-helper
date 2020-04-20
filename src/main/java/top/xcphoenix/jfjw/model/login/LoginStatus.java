package top.xcphoenix.jfjw.model.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author      xuanc
 * @date        2020/4/15 下午9:31
 * @version     1.0
 */
@Getter
@Setter
@ToString
public class LoginStatus {

    private boolean success;
    private String errorMsg;

    private static final String DEFAULT_MSG = "login failed";

    private LoginStatus(boolean success, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public static LoginStatus success() {
        return new LoginStatus(true, null);
    }

    public static LoginStatus error(String msg) {
        if (msg == null) {
            msg = DEFAULT_MSG;
        }
        return new LoginStatus(false, msg);
    }

}
