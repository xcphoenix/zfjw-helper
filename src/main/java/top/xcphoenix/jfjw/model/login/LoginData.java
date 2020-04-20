package top.xcphoenix.jfjw.model.login;

import lombok.Data;
import lombok.Getter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import top.xcphoenix.jfjw.model.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录信息
 *
 * @author      xuanc
 * @date        2020/4/13 下午9:48
 * @version     1.0
 */
@Data
public class LoginData {

    private String csrfToken;
    private User user;

    public LoginData(String csrfToken, User user) {
        this.csrfToken = csrfToken;
        this.user = user;
    }

    @Getter
    public static class CovertData {

        private NameValuePair[] urlParams;
        private List<NameValuePair> requestEntity;

        private CovertData(NameValuePair[] urlParams, List<NameValuePair> requestEntity) {
            this.urlParams = urlParams;
            this.requestEntity = requestEntity;
        }

        public static CovertData buildCovertData(LoginData loginData, String encryptedPassword) {
            /*
             * 当前 url 为时间戳，在 UrlManagerImpl#
             */
            NameValuePair[] urlParams = null;

            List<NameValuePair> requestEntity = new ArrayList<>();
            requestEntity.add(new BasicNameValuePair("csrftoken", loginData.getCsrfToken()));
            requestEntity.add(new BasicNameValuePair("yhm", loginData.getUser().getStuCode()));
            requestEntity.add(new BasicNameValuePair("mm", encryptedPassword));
            requestEntity.add(new BasicNameValuePair("mm", encryptedPassword));

            return new CovertData(urlParams, requestEntity);
        }

    }

}
