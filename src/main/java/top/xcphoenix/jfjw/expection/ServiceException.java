package top.xcphoenix.jfjw.expection;

/**
 * @author      xuanc
 * @date        2020/4/17 上午11:10
 * @version     1.0
 */ 
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
