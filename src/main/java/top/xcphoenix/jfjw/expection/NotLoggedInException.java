package top.xcphoenix.jfjw.expection;

/**
 * @author      xuanc
 * @date        2020/4/20 上午9:57
 * @version     1.0
 */ 
public class NotLoggedInException extends LoginException {

    public NotLoggedInException() {
        super();
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

}
