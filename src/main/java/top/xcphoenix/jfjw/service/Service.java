package top.xcphoenix.jfjw.service;

import top.xcphoenix.jfjw.model.LoginStatus;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 下午10:32
 */
public interface Service {

    /**
     * 返回登录状态
     */
    public LoginStatus getLoginStatus();

}
