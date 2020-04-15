package top.xcphoenix.jfjw.service;

import org.apache.http.client.CookieStore;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.model.LoginStatus;
import top.xcphoenix.jfjw.model.User;


/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 上午9:25
 */
public interface LoginService {

    /**
     * 登录
     * @return 登录信息
     */
    LoginStatus login() throws PublicKeyException, LoginException;

    /**
     * 获取 cookie
     * @return cookie 值
     */
    CookieStore getCookie();

}
