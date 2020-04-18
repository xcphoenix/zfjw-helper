package top.xcphoenix.jfjw.service.core.impl;

import lombok.Setter;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.manager.impl.KeyManagerImpl;
import top.xcphoenix.jfjw.model.LoginData;
import top.xcphoenix.jfjw.model.LoginStatus;
import top.xcphoenix.jfjw.model.User;
import top.xcphoenix.jfjw.service.BaseService;
import top.xcphoenix.jfjw.service.core.LoginService;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午9:27
 */
@Setter
public class LoginServiceImpl extends BaseService implements LoginService {

    private KeyManager keyManager = KeyManagerImpl.getInstance();

    @Override
    public LoginStatus login(User user) throws LoginException {
        LoginData.CovertData covertData = sendIndex(user);
        return sendLogin(covertData);
    }

    protected LoginData.CovertData sendIndex(User user) throws LoginException {
        String password;
        LoginData loginData;
        CloseableHttpResponse response = null;

        try {
            HttpGet httpGet = new HttpGet(urlManager.getIndexLink(domain).build());
            response = httpClient.execute(httpGet, context);

            Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            Element element = document.getElementById("csrftoken");
            String csrfToken;
            if (element == null || (csrfToken = element.attr("value")) == null
                    || csrfToken.length() == 0) {
                throw new LoginException("csrftoken not found on index page, page content: " + document.outerHtml());
            }

            loginData = new LoginData(csrfToken, user);
            password = keyManager.encryptPassword(
                    keyManager.getPublicKey(domain, context),
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
            uriBuilder = urlManager.getLoginLink(domain);
            if (covertData.getUrlParams() != null) {
                uriBuilder.setParameters(covertData.getUrlParams());
            }
            httpPost = new HttpPost(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new LoginException("invalid login url", e);
        }

        httpPost.setEntity(new UrlEncodedFormEntity(covertData.getRequestEntity(), Consts.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(httpPost, context)) {
            return getLoginStatusFromResp(response, EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new LoginException("IO error");
        }
    }

    protected LoginStatus getLoginStatusFromResp(HttpResponse response, String html) {
        int successStatusCode = 302;
        /*
         * error tag
         */
        String tipsId = "tips";
        String errorClass = "bg_danger sl_danger";

        /*
         * 通过重定向状态码获取登录信息
         */
        if (response.getStatusLine().getStatusCode() == successStatusCode) {
            return LoginStatus.success();
        }
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

}
