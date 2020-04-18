package top.xcphoenix.jfjw.service;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import top.xcphoenix.jfjw.manager.UrlManager;
import top.xcphoenix.jfjw.manager.impl.UrlManagerImpl;
import top.xcphoenix.jfjw.util.HttpClientUtils;

import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

/**
 * 服务抽象类，调用具体服务必须先使用 <code>init()</code> 方法初始化<br/>
 * 建议使用 <code>Services.create()<code/> 创建服务
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 上午10:46
 */
public abstract class BaseService {

    protected String domain;
    protected HttpClientContext context;

    /** url 处理器 */
    protected UrlManager urlManager = UrlManagerImpl.getInstance();
    protected CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();

    /* -- public method -- */

    /**
     * 初始化服务
     *
     * @param domain 域名
     * @return 服务
     */
    public BaseService init(String domain) {
        if (validateDomain(domain)) {
            throw new RuntimeException("domain invalid");
        }
        this.domain = domain;
        this.context = HttpClientContext.create();
        this.context.setCookieStore(new BasicCookieStore());
        return this;
    }

    /**
     * 初始化服务
     *
     * @param domain 域名
     * @param cookieStore cookie 存储
     * @return 服务
     */
    public BaseService init(String domain, CookieStore cookieStore) {
        if (validateDomain(domain)) {
            throw new RuntimeException("domain invalid");
        }
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        this.domain = domain;
        this.context = HttpClientContext.create();
        this.context.setCookieStore(cookieStore);
        return this;
    }

    /**
     * 设置 url 处理器
     */
    public void setUrlManager(UrlManager urlManager) {
        this.urlManager = urlManager;
    }

    /* Getter */

    public HttpClientContext getContext() {
        return context;
    }

    /* -- protected for subclass -- */

    protected boolean isNeedLogin(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
                && response.getFirstHeader("Location").getValue()
                .startsWith(urlManager.getLoginRedirectLink());
    }

    /**
     * 域名校验
     */
    protected boolean validateDomain(String domain) {
        String domainRule = "^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?$";
        return !Pattern.matches(domainRule, domain);
    }

}
