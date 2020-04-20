package top.xcphoenix.jfjw.service.core;

import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.model.user.UserBaseInfo;

/**
 * 获取用户信息
 *
 * @author      xuanc
 * @date        2020/4/17 上午10:06
 * @version     1.0
 */ 
public interface UserInfoService {

    /**
     * 获取用户信息
     *
     * @return 用户信息
     * @throws NotLoggedInException 未登录
     */
    UserBaseInfo getUserInfo() throws NotLoggedInException;

}
