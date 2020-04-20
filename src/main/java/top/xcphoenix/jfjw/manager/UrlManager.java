package top.xcphoenix.jfjw.manager;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/14 下午10:41
 */
public interface UrlManager {

    /**
     * 获取重定向url
     *
     * @return 登录地址
     */
    String getLoginRedirectLink();

    /**
     * 获取公钥uri
     *
     * @param baseUrl 域名
     * @return uri
     * @throws URISyntaxException url语法错误（无效的域名）
     */
    URIBuilder getPublicKeyLink(String baseUrl) throws URISyntaxException;

    /**
     * 获取首页url
     *
     * @param baseUrl 域名
     * @return url
     * @throws URISyntaxException url语法错误（无效的域名）
     */
    URIBuilder getIndexLink(String baseUrl) throws URISyntaxException;

    /**
     * 获取登录url
     *
     * @param baseUrl 域名
     * @return url
     * @throws URISyntaxException url语法错误（无效的域名）
     */
    URIBuilder getLoginLink(String baseUrl) throws URISyntaxException;

    /**
     * 获取用户信息url
     *
     * @param baseUrl 域名
     * @return url
     * @throws URISyntaxException url语法错误（无效的域名）
     */
    URIBuilder getUserInfoApiLink(String baseUrl) throws URISyntaxException;

    /**
     * 获取课表信息url
     *
     * @param baseUrl 域名
     * @return url
     * @throws URISyntaxException url语法错误（无效的域名）
     */
    URIBuilder getClassTableApiLink(String baseUrl) throws URISyntaxException;

    URIBuilder getClassTableSimplePage(String baseUrl) throws URISyntaxException;
}
