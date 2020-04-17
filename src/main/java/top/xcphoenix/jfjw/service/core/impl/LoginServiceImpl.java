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
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.manager.impl.KeyManagerImpl;
import top.xcphoenix.jfjw.model.LoginData;
import top.xcphoenix.jfjw.model.LoginStatus;
import top.xcphoenix.jfjw.model.User;
import top.xcphoenix.jfjw.service.AbstractService;
import top.xcphoenix.jfjw.service.core.LoginService;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午9:27
 */
@Setter
public class LoginServiceImpl extends AbstractService implements LoginService {

    private KeyManager keyManager = KeyManagerImpl.getInstance();

    @Override
    public void login(User user) throws LoginException {
        try {
            LoginData.CovertData covertData = sendIndex(user);
            parse(sendLogin(covertData));
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected LoginData.CovertData sendIndex(User user) throws LoginException {
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

    protected HttpResponse sendLogin(LoginData.CovertData covertData) throws LoginException {
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

        try {
            return httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new LoginException("IO error");
        }
    }

    @Override
    protected Void parse(HttpResponse response) {
        return null;
    }

    @Override
    protected LoginStatus getLoginStatusFromResp(HttpResponse response) {
        int successStatusCode = 302;

        /*
         * 通过重定向状态码获取登录信息
         */
        if (response.getStatusLine().getStatusCode() == successStatusCode) {
            return LoginStatus.success();
        }
        try {
            LoginStatus loginStatus = super.getLoginStatusFromResp(response);
            loginStatus.setSuccess(false);
            return loginStatus;
        } catch (IOException e) {
            e.printStackTrace();
            return LoginStatus.error(null);
        }
    }

}
