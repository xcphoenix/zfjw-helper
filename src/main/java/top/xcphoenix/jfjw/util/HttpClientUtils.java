package top.xcphoenix.jfjw.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author      xuanc
 * @date        2020/4/18 上午11:12
 * @version     1.0
 */ 
public class HttpClientUtils {

    private static CloseableHttpClient httpClient;

    private HttpClientUtils() {}

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientUtils.class) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom()
                            .disableRedirectHandling()
                            .build();
                }
            }
        }
        return httpClient;
    }

}
