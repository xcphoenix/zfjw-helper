package top.xcphoenix.jfjw.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.expection.ServiceException;
import top.xcphoenix.jfjw.model.UserBaseInfo;
import top.xcphoenix.jfjw.service.BaseService;
import top.xcphoenix.jfjw.service.core.UserInfoService;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 下午10:02
 */
public class UserInfoServiceImpl extends BaseService implements UserInfoService {

    @Override
    public UserBaseInfo getUserInfo() throws LoginException, ServiceException {
        URIBuilder uriBuilder;
        HttpGet httpGet;

        try {
            uriBuilder = urlManager.getUserInfoApiLink(domain);
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new ServiceException("the user info url is invalid", e);
        }
        try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {
            return dealResp(response);
        } catch (IOException e) {
            throw new ServiceException("io error", e);
        }
    }

    private UserBaseInfo dealResp(CloseableHttpResponse response) throws LoginException {
        if (super.isNeedLogin(response)) {
            throw new LoginException("need login");
        }

        String html;
        try {
            html = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new ServiceException("io error", e);
        }
        UserBaseInfo info;
        try {
            info = getUserInfo(html);
        } catch (JSONException e) {
            throw new ServiceException("services error");
        }
        return info;
    }

    private UserBaseInfo getUserInfo(String json) throws JSONException {
        JSONObject jsonObject = JSON.parseObject(json);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setAllData(jsonObject.getInnerMap());

        userBaseInfo.setStuCode(jsonObject.getString("xh"));
        userBaseInfo.setName(jsonObject.getString("xm"));
        userBaseInfo.setSex(jsonObject.getString("xbm"));
        userBaseInfo.setHomeTown(jsonObject.getString("jg"));
        userBaseInfo.setGrade(jsonObject.getIntValue("njdm_id"));
        userBaseInfo.setIClass(jsonObject.getString("bh_id"));
        userBaseInfo.setMajor(jsonObject.getString("zyh_id"));
        userBaseInfo.setFaculty(jsonObject.getString("zsjg_id"));
        userBaseInfo.setKind(jsonObject.getString("xslbdm"));
        return userBaseInfo;
    }

}
