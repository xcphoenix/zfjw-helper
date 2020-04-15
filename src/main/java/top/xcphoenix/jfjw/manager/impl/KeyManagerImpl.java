package top.xcphoenix.jfjw.manager.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.util.B64;
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
    public RSA getPublicKey(String baseUrl) throws
            PublicKeyException {
        return getPublicKey(baseUrl, null, null);
    }

    @Override
    public RSA getPublicKey(String url, CookieStore cookieStore) throws PublicKeyException {
        return getPublicKey(url, null, cookieStore);
    }

    @Override
    public RSA getPublicKey(String url, Map<String, String> headers) throws PublicKeyException {
        return getPublicKey(url, headers, null);
    }

    @Override
    public RSA getPublicKey(String baseUrl, Map<String, String> headers, CookieStore cookieStore)
            throws PublicKeyException {

        String modulus;
        String exponent;

        URIBuilder uriBuilder = null;
        HttpGet httpGet;

        try {
            uriBuilder = UrlManagerImpl.getInstance().getPublicKeyUrl(baseUrl);
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

        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient httpClient = cookieStore == null ? builder.build() :
                builder.setDefaultCookieStore(cookieStore).build();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
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
                httpClient.close();
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
