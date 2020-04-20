package top.xcphoenix.jfjw.expection;

/**
 * @author      xuanc
 * @date        2020/4/14 下午11:35
 * @version     1.0
 */ 
public class LoginException extends Exception {

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

}
