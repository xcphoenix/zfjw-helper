package top.xcphoenix.jfjw.manager;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 下午10:41
 */
public interface UrlManager {

    public URIBuilder getPublicKeyUrl(String baseUrl) throws URISyntaxException;

    public URIBuilder getIndexUrl(String baseUrl) throws URISyntaxException;

    URIBuilder getLoginUrl(String baseUrl) throws URISyntaxException;
}
