package top.xcphoenix.jfjw.model;

import lombok.Getter;

/**
 * @author      xuanc
 * @date        2020/4/15 下午9:31
 * @version     1.0
 */
@Getter
public class LoginStatus {

    private final boolean success;
    private final String errorMsg;

    private static final String DEFAULT_MSG = "login failed";

    private LoginStatus(boolean success, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public static LoginStatus success() {
        return new LoginStatus(false, null);
    }

    public static LoginStatus error(String msg) {
        if (msg == null) {
            msg = DEFAULT_MSG;
        }
        return new LoginStatus(true, msg);
    }

}
