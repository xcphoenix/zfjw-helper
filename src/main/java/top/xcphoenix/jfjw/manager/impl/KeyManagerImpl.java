package top.xcphoenix.jfjw.manager.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.util.B64;
import top.xcphoenix.jfjw.util.HttpClientUtils;
import top.xcphoenix.jfjw.util.RSA;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午8:46
 */
public class KeyManagerImpl implements KeyManager {

    private KeyManagerImpl() {
    }

    private static KeyManager instance;

    private static final String MODULUS = "modulus";
    private static final String EXPONENT = "exponent";

    public static KeyManager getInstance() {
        if (instance == null) {
            synchronized (KeyManagerImpl.class) {
                if (instance == null) {
                    instance = new KeyManagerImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public RSA getPublicKey(String baseUrl) {
        return getPublicKey(baseUrl, null, null);
    }

    @Override
    public RSA getPublicKey(String url, HttpClientContext context) {
        return getPublicKey(url, null, context);
    }

    @Override
    public RSA getPublicKey(String url, Map<String, String> headers) {
        return getPublicKey(url, headers, null);
    }

    @Override
    public RSA getPublicKey(String baseUrl, Map<String, String> headers, HttpClientContext context) {

        String modulus;
        String exponent;

        URIBuilder uriBuilder = null;
        HttpGet httpGet;

        try {
            uriBuilder = UrlManagerImpl.getInstance().getPublicKeyLink(baseUrl);
            httpGet = new HttpGet(uriBuilder.build());
            if (headers != null && !headers.isEmpty()) {
                httpGet.setHeaders(
                        headers.entrySet().stream().map(
                                o -> new BasicHeader(o.getKey(), o.getValue())
                        ).toArray(Header[]::new)
                );
            }
        } catch (URISyntaxException e) {
            throw new PublicKeyException("publicKeyUrl is invalid, url: " +
                    (uriBuilder != null ? uriBuilder.toString() : "null"), e);
        }

        CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
            JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            modulus = B64.b64ToHex(jsonObject.getString(MODULUS));
            exponent = B64.b64ToHex(jsonObject.getString(EXPONENT));
        } catch (IOException e) {
            throw new PublicKeyException("IO error happened on get publicKeyUrl processor: url: " + uriBuilder.toString(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new RSA(modulus, exponent);
    }

    @Override
    public String encryptPassword(RSA rsa, String value) {
        return B64.hex2b64(rsa.RSAEncrypt(value));
    }

}
