package top.xcphoenix.jfjw.service.core.impl;

import lombok.Setter;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
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
import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.expection.ServiceException;
import top.xcphoenix.jfjw.manager.KeyManager;
import top.xcphoenix.jfjw.manager.impl.KeyManagerImpl;
import top.xcphoenix.jfjw.model.login.LoginData;
import top.xcphoenix.jfjw.model.login.LoginStatus;
import top.xcphoenix.jfjw.model.user.User;
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
            return getLoginStatusFromResp(response);
        } catch (IOException e) {
            throw new LoginException("IO error");
        }
    }

    private LoginStatus getLoginStatusFromResp(CloseableHttpResponse response) {
        try {
            getResp(response);
        } catch (IOException ioException) {
            throw new ServiceException("io error", ioException);
        } catch (NotLoggedInException e) {
            return e.getStatus();
        }
        return LoginStatus.success();
    }

    @Override
    protected boolean isNeedLogin(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY;
    }

}
