package top.xcphoenix.jfjw.service.core.impl;

import org.apache.http.HttpResponse;
import top.xcphoenix.jfjw.model.UserBaseInfo;
import top.xcphoenix.jfjw.service.AbstractService;
import top.xcphoenix.jfjw.service.core.UserInfoService;

/**
 * @author      xuanc
 * @date        2020/4/17 下午10:02
 * @version     1.0
 */ 
public class UserInfoServiceImpl extends AbstractService implements UserInfoService {

    /**
     * 登录受检
     */
    private static final boolean loginChecked = true;

    @Override
    public UserBaseInfo getUserInfo() {
        return null;
    }

    @Override
    protected Object parse(HttpResponse response) {
        return null;
    }
}
