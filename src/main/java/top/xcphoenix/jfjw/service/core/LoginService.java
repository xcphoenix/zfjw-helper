package top.xcphoenix.jfjw.service.core;

import top.xcphoenix.jfjw.expection.LoginException;
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
     *
     * @param user 用户信息
     * @return 登录状态
     * @throws LoginException URL错误或网络错误
     */
    LoginStatus login(User user) throws LoginException;

}
