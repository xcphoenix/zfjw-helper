package top.xcphoenix.jfjw.service;

import lombok.NonNull;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.xcphoenix.jfjw.config.ServiceConfig;
import top.xcphoenix.jfjw.expection.ConfigException;
import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.manager.UrlManager;
import top.xcphoenix.jfjw.manager.impl.UrlManagerImpl;
import top.xcphoenix.jfjw.model.login.LoginStatus;
import top.xcphoenix.jfjw.util.HttpClientUtils;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 服务抽象类，调用具体服务必须先使用 <code>init()</code> 方法初始化<br/>
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 上午10:46
 */
public abstract class BaseService {

    protected String domain;
    protected HttpClientContext context;

    protected UrlManager urlManager = UrlManagerImpl.getInstance();
    protected CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?$");

    public BaseService() {
        this(ServiceConfig.getGlobalConfig());
    }

    public BaseService(ServiceConfig config) {
        if (config == null) {
            throw new ConfigException("cannot find global config");
        }
        init(config);
    }

    /**
     * 初始化服务
     *
     * @param config 配置信息
     */
    protected void init(ServiceConfig config) {
        if (!validateDomain(config.getDomain())) {
            throw new RuntimeException("domain invalid");
        }
        this.domain = config.getDomain();
        this.context = HttpClientContext.create();
        this.context.setCookieStore(config.getCookieStore());
    }

    protected String getResp(@NonNull CloseableHttpResponse response)
            throws IOException, NotLoggedInException {
        String page = EntityUtils.toString(response.getEntity());
        if (isNeedLogin(response)) {
            throw new NotLoggedInException(getErrLoginStatus(page));
        }
        return page;
    }

    protected boolean isNeedLogin(@NonNull CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
                && response.getFirstHeader("Location").getValue()
                .startsWith(urlManager.getLoginRedirectLink());
    }

    protected LoginStatus getErrLoginStatus(String html) {
        // error tag
        String tipsId = "tips";
        String errorClass = "bg_danger sl_danger";

        Document document = Jsoup.parse(html);
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
        return LoginStatus.error(msg);
    }

    /**
     * 域名校验
     */
    private boolean validateDomain(@NonNull String domain) {
        return domain != null && DOMAIN_PATTERN.matcher(domain).matches();
    }

}
