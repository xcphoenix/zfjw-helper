package top.xcphoenix.jfjw.manager;

import org.apache.http.client.protocol.HttpClientContext;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.util.RSA;

import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午8:27
 */
public interface KeyManager {


    /**
     * @see #getPublicKey(String, Map, HttpClientContext)
     */
    RSA getPublicKey(String url);

    /**
     * @see #getPublicKey(String, Map, HttpClientContext)
     */
    RSA getPublicKey(String url, HttpClientContext context);

    /**
     * @see #getPublicKey(String, Map, HttpClientContext)
     */
    RSA getPublicKey(String url, Map<String, String> headers);

    /**
     * 获取公钥
     *
     * @param baseUrl     链接前缀
     * @param headers     自定义请求头
     * @param context     httpClient context
     * @return 公钥
     * @throws PublicKeyException 获取异常
     */
    RSA getPublicKey(String baseUrl, Map<String, String> headers, HttpClientContext context);

    /**
     * 使用公钥加密
     *
     * @param rsa RSA
     * @param value 要加密的信息
     * @return 加密后的信息
     */
    String encryptPassword(RSA rsa, String value);

}
