package top.xcphoenix.jfjw.expection;

/**
 * @author      xuanc
 * @date        2020/4/14 下午4:57
 * @version     1.0
 */ 
public class PublicKeyException extends RuntimeException {

    public PublicKeyException() {
        super();
    }

    public PublicKeyException(String message) {
        super(message);
    }

    public PublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublicKeyException(Throwable cause) {
        super(cause);
    }

}
