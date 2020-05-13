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
    private static ServiceConfig globalServiceConfig;

    private String domain;
    private CookieStore cookieStore;

    public ServiceConfig(String domain) {
        this.domain = domain;
        this.cookieStore = new BasicCookieStore();
    }

    public ServiceConfig(String domain, CookieStore cookieStore) {
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        this.domain = domain;
        this.cookieStore = cookieStore;
    }

    /**
     * init config
     *
     * @param domain 域名
     * @return config
     * @throws ConfigException 若全局配置非空时，抛出异常
     */
    public static ServiceConfig buildGlobal(String domain) throws ConfigException {
        ServiceConfig serviceConfig = new ServiceConfig(domain, new BasicCookieStore());
        if (globalServiceConfig != null) {
            throw new ConfigException("config serviceConfig not empty, danger operation");
        }
        globalServiceConfig = serviceConfig;
        return serviceConfig;
    }

    /**
     * init config
     *
     * @param domain      域名
     * @param cookieStore cookie存储
     * @return config
     * @throws ConfigException 若全局配置非空时，抛出异常
     */
    public ServiceConfig buildGlobal(String domain, CookieStore cookieStore) throws ConfigException {
        ServiceConfig serviceConfig = new ServiceConfig(domain, cookieStore);
        if (globalServiceConfig != null) {
            throw new ConfigException("config serviceConfig not empty, danger operation");
        }
        globalServiceConfig = serviceConfig;
        return serviceConfig;
    }

    /**
     * 获取全局配置类
     *
     * @return 全局配置
     */
    public static ServiceConfig getGlobalServiceConfig() {
        return globalServiceConfig;
    }

    /**
     * clean config config
     */
    public static void clean() {
        globalServiceConfig = null;
    }

}
