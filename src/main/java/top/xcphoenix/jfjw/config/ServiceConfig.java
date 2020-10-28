package top.xcphoenix.jfjw.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import top.xcphoenix.jfjw.expection.ConfigException;

/**
 * 全局配置信息
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/17 上午10:26
 */
@Getter
@Setter
public class ServiceConfig {

    /**
     * 全局配置
     */
    private static ServiceConfig globalConfig;

    private String domain;
    private CookieStore cookieStore;

    public ServiceConfig(String domain) {
        this.domain = domain;
        this.cookieStore = new BasicCookieStore();
    }

    /**
     * init config
     *
     * @param domain      域名
     * @throws ConfigException 若全局配置非空时，抛出异常
     */
    public static void buildGlobal(String domain) throws ConfigException {
        if (globalConfig != null) {
            throw new ConfigException("config serviceConfig not empty, danger operation");
        }
        globalConfig = new ServiceConfig(domain);
    }

    /**
     * 获取全局配置类
     *
     * @return 全局配置
     */
    public static ServiceConfig getGlobalConfig() {
        return globalConfig;
    }

    /**
     * clean config config
     */
    public static void clean() {
        globalConfig = null;
    }

}
