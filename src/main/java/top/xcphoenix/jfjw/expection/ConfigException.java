package top.xcphoenix.jfjw.expection;

/**
 * @author      xuanc
 * @date        2020/4/17 上午10:36
 * @version     1.0
 */ 
public class ConfigException extends Exception {

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    protected ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
