package top.xcphoenix.jfjw.expection;

import top.xcphoenix.jfjw.model.login.LoginStatus;

/**
 * @author      xuanc
 * @date        2020/4/20 上午9:57
 * @version     1.0
 */ 
public class NotLoggedInException extends LoginException {

    private LoginStatus status;

    public NotLoggedInException(LoginStatus status) {
        this();
        this.status = status;
    }

    public NotLoggedInException() {
        super();
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginStatus getStatus() {
        return status;
    }
}
