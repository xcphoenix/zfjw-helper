package top.xcphoenix.jfjw.service.impl;

import lombok.Setter;
import org.apache.http.Consts;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.manager.UrlManager;
import top.xcphoenix.jfjw.manager.impl.KeyManagerImpl;
import top.xcphoenix.jfjw.manager.impl.UrlManagerImpl;
import top.xcphoenix.jfjw.model.LoginStatus;
import top.xcphoenix.jfjw.model.LoginData;
import top.xcphoenix.jfjw.model.User;
import top.xcphoenix.jfjw.service.LoginService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午9:27
 */
@Setter
public class LoginServiceImpl implements LoginService {

    private String domain;
    private User user;

    private UrlManager urlManager = UrlManagerImpl.getInstance();
    private KeyManager keyManager = KeyManagerImpl.getInstance();

    // store cookies
    private CookieStore cookieStore = new BasicCookieStore();
    // set auto redirect
    private CloseableHttpClient httpClient = HttpClients.custom()
            .disableRedirectHandling()
            .setDefaultCookieStore(cookieStore)
            .build();

    public LoginServiceImpl(String domain, User user) {
        if (!validateDomain(domain)) {
            throw new RuntimeException("domain invalid");
        }
        this.domain = domain;
        this.user = user;
    }

    @Override
    public LoginStatus login() throws PublicKeyException, LoginException {
        try {
            LoginData.CovertData covertData = sendIndex();
            return sendLogin(covertData);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CookieStore getCookie() {
        return cookieStore;
    }

    protected LoginData.CovertData sendIndex() throws PublicKeyException, LoginException {
        String password;
        LoginData loginData;
        CloseableHttpResponse response = null;

        try {
            HttpGet httpGet = new HttpGet(urlManager.getIndexUrl(domain).build());
            response = httpClient.execute(httpGet);

            Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            Element element = document.getElementById("csrftoken");
            String csrfToken;
            if (element == null || (csrfToken = element.attr("value")) == null
                    || csrfToken.length() == 0) {
                throw new LoginException("csrftoken not found on index page, page content: " + document.outerHtml());
            }

            loginData = new LoginData(csrfToken, user);
            password = keyManager.encryptPassword(
                    keyManager.getPublicKey(domain, cookieStore),
                    user.getPassword()
            );
        } catch (IOException | URISyntaxException e) {
            String msg = e instanceof URISyntaxException ? "invalid index url" : "login failed";
            throw new LoginException(msg, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return LoginData.CovertData.buildCovertData(loginData, password);
    }

    protected LoginStatus sendLogin(LoginData.CovertData covertData) throws LoginException {
        URIBuilder uriBuilder;
        HttpPost httpPost;

        try {
            uriBuilder = urlManager.getLoginUrl(domain);
            if (covertData.getUrlParams() != null) {
                uriBuilder.setParameters(covertData.getUrlParams());
            }
            httpPost = new HttpPost(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new LoginException("invalid login url", e);
        }

        httpPost.setEntity(new UrlEncodedFormEntity(covertData.getRequestEntity(), Consts.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return getLoginStatus(response);
        } catch (IOException e) {
            e.printStackTrace();
            return LoginStatus.error("服务异常");
        }
    }

    protected boolean validateDomain(String domain) {
        String domainRule = "^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?$";
        return Pattern.matches(domainRule, domain);
    }

    protected LoginStatus getLoginStatus(CloseableHttpResponse response) {
        int successStatusCode = 302;
        String tipsId = "tips";
        String errorClass = "bg_danger sl_danger";

        if (response.getStatusLine().getStatusCode() == successStatusCode) {
            return LoginStatus.success();
        }
        try {
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
            return LoginStatus.error(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return LoginStatus.error(null);
        }
    }

}
