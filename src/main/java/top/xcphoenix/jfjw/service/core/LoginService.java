package top.xcphoenix.jfjw.service.core;

import org.apache.http.client.CookieStore;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.model.LoginStatus;
import top.xcphoenix.jfjw.model.User;
import top.xcphoenix.jfjw.service.Service;


/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午9:25
 */
public interface LoginService extends Service {

    /**
     * 登录
     *
     * @param user 用户信息
     */
    void login(User user) throws LoginException;

}
