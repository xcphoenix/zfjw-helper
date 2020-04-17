package top.xcphoenix.jfjw.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.xcphoenix.jfjw.manager.UrlManager;
import top.xcphoenix.jfjw.manager.impl.UrlManagerImpl;
import top.xcphoenix.jfjw.model.LoginStatus;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 服务抽象类，调用具体服务必须先使用 <code>init()</code> 方法初始化<br/>
 * 建议使用 <code>Services.create()<code/> 创建服务
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 上午10:46
 */
public abstract class AbstractService implements Service {

    protected String domain;
    protected CookieStore cookieStore;

    protected CloseableHttpClient httpClient;
    /** url 处理器 */
    protected UrlManager urlManager = UrlManagerImpl.getInstance();
    /** 登录状态 */
    protected LoginStatus loginStatus;



    /**
     * 初始化服务
     *
     * @param domain 域名
     * @return 服务
     */
    public AbstractService init(String domain) {
        if (validateDomain(domain)) {
            throw new RuntimeException("domain invalid");
        }
        this.domain = domain;
        this.cookieStore = new BasicCookieStore();
        // set auto redirect
        this.httpClient = HttpClients.custom()
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore)
                .build();
        return this;
    }

    /**
     * 初始化服务
     *
     * @param domain 域名
     * @param cookieStore cookie 存储
     * @return 服务
     */
    public AbstractService init(String domain, CookieStore cookieStore) {
        if (validateDomain(domain)) {
            throw new RuntimeException("domain invalid");
        }
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        this.domain = domain;
        this.cookieStore = cookieStore;
        // set auto redirect
        this.httpClient = HttpClients.custom()
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore)
                .build();
        return this;
    }

    /**
     * 设置 url 处理器
     */
    public void setUrlManager(UrlManager urlManager) {
        this.urlManager = urlManager;
    }


    /**
     * 页面解析
     */
    protected abstract Object parse(HttpResponse response);


    /**
     * 返回登录状态
     */
    @Override
    public LoginStatus getLoginStatus() {
        return loginStatus;
    }



    /**
     * 如果是 json 一定返回 <code>LoginStatus.success()</code> <br />
     * 通过 response 数据判断登录状态
     */
    protected LoginStatus getLoginStatusFromResp(HttpResponse response) throws IOException {
        /*
         * error tag
         */
        String tipsId = "tips";
        String errorClass = "bg_danger sl_danger";

        Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));
        Element element = document.getElementById(tipsId);
        String msg = null;
        if (element == null) {
            Elements elements = document.getElementsByClass(errorClass);
            if (elements != null && !elements.isEmpty()) {
                msg = String.join(",", elements.eachText());
            }
        } else {
            msg = element.text();
        }
        return msg == null ? LoginStatus.success() : LoginStatus.error(msg);
    }

    /**
     * 域名校验
     */
    protected boolean validateDomain(String domain) {
        String domainRule = "^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?$";
        return !Pattern.matches(domainRule, domain);
    }

}
