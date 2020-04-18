package top.xcphoenix.jfjw.manager.impl;


import org.apache.http.client.utils.URIBuilder;
import top.xcphoenix.jfjw.manager.UrlManager;

import java.net.URISyntaxException;

/**
 * @author      xuanc
 * @date        2020/4/14 上午9:45
 * @version     1.0
 */ 
public class UrlManagerImpl implements UrlManager {

    private static final String defaultProtocol = "http";

    private static final String publicKeyUrl = "/jwglxt/xtgl/login_getPublicKey.html";
    private static final String indexUrl = "/jwglxt/xtgl/login_slogin.html";
    private static final String loginUrl = "/jwglxt/xtgl/login_slogin.html";
    private static final String userInfoApiUrl = "/jwglxt/xsxxxggl/xsxxwh_cxCkDgxsxx.html?gnmkdm=N100801";

    private UrlManagerImpl() {
    }

    private static UrlManager instance;

    public static UrlManager getInstance() {
        if (instance == null) {
            synchronized (UrlManagerImpl.class) {
                if (instance == null) {
                    instance = new UrlManagerImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public URIBuilder getPublicKeyLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, publicKeyUrl), true);
    }

    @Override
    public URIBuilder getIndexLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, indexUrl), false)
                .setParameter("_t", String.valueOf(System.currentTimeMillis()))
                .setParameter("language", "zh_CN");
    }

    @Override
    public URIBuilder getLoginLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, loginUrl), true);
    }

    @Override
    public URIBuilder getUserInfoApiLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, userInfoApiUrl), false);
    }

    @Override
    public String getLoginRedirectLink() {
        return loginUrl;
    }

    private String getUrl(String baseUrl, String link) {
        return defaultProtocol + "://" +
                (!baseUrl.endsWith("/") ? baseUrl : baseUrl.substring(0, baseUrl.length() - 1))
                + link;
    }

    private URIBuilder afterDecorate(String string, boolean exec) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(string);
        if (!exec) {
            return uriBuilder;
        }
        uriBuilder.setParameter("time", String.valueOf(System.currentTimeMillis()));
        return uriBuilder;
    }

}
