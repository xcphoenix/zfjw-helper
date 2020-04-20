package top.xcphoenix.jfjw.manager.impl;


import org.apache.http.client.utils.URIBuilder;
import top.xcphoenix.jfjw.manager.UrlManager;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author      xuanc
 * @date        2020/4/14 上午9:45
 * @version     1.0
 */ 
public class UrlManagerImpl implements UrlManager {

    private static final String DEFAULT_PROTOCOL = "http";

    private static final String PUBLIC_KEY_URL = "/jwglxt/xtgl/login_getPublicKey.html";
    private static final String INDEX_URL = "/jwglxt/xtgl/login_slogin.html";
    private static final String LOGIN_URL = "/jwglxt/xtgl/login_slogin.html";
    private static final String USER_INFO_API_URL = "/jwglxt/xsxxxggl/xsxxwh_cxCkDgxsxx.html";
    private static final String CLASS_TABLE_API_URL = "/jwglxt/kbcx/xskbcx_cxXsKb.html";
    /**
     * for get current year and period
     */
    private static final String CLASS_TABLE_SIMPLE_PAGE = "/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html";

    /*
     * singleton
     */

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

    /** public method */

    @Override
    public URIBuilder getPublicKeyLink(String baseUrl) throws URISyntaxException {
        return afterCurrTimeDecorate(getUrl(baseUrl, PUBLIC_KEY_URL));
    }

    @Override
    public URIBuilder getIndexLink(String baseUrl) throws URISyntaxException {
        return afterCurrTimeDecorate(getUrl(baseUrl, INDEX_URL), "_t")
                .setParameter("language", "zh_CN");
    }

    @Override
    public URIBuilder getLoginLink(String baseUrl) throws URISyntaxException {
        return afterCurrTimeDecorate(getUrl(baseUrl, LOGIN_URL));
    }

    @Override
    public URIBuilder getUserInfoApiLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, USER_INFO_API_URL))
                .setParameter("gnmkdm", "N100801");
    }

    @Override
    public String getLoginRedirectLink() {
        return LOGIN_URL;
    }

    @Override
    public URIBuilder getClassTableApiLink(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, CLASS_TABLE_API_URL))
                .setParameter("gnmkdm", "N253508");
    }

    @Override
    public URIBuilder getClassTableSimplePage(String baseUrl) throws URISyntaxException {
        return afterDecorate(getUrl(baseUrl, CLASS_TABLE_SIMPLE_PAGE))
                .setParameter("gnmkdm", "N253508");
    }


    /* private */

    protected String getUrl(String baseUrl, String link) {
        return DEFAULT_PROTOCOL + "://" +
                (!baseUrl.endsWith("/") ? baseUrl : baseUrl.substring(0, baseUrl.length() - 1))
                + link;
    }

    protected URIBuilder afterDecorate(String string) throws URISyntaxException {
        return new URIBuilder(string);
    }

    protected URIBuilder afterCurrTimeDecorate(String string) throws URISyntaxException {
        return afterCurrTimeDecorate(string, "time");
    }

    protected URIBuilder afterCurrTimeDecorate(String string, String key) throws URISyntaxException {
        URIBuilder uriBuilder = afterDecorate(string);
        if (key == null) {
            return uriBuilder;
        }
        uriBuilder.setParameter(key, String.valueOf(System.currentTimeMillis()));
        return uriBuilder;
    }

}
