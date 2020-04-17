package top.xcphoenix.jfjw.global;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import top.xcphoenix.jfjw.expection.ConfigException;

import java.util.List;

/**
 * 全局配置信息
 *
 * @author      xuanc
 * @date        2020/4/17 上午10:26
 * @version     1.0
 */
@Getter
@Setter
public class Config {

    /**
     * 全局配置
     */
    private static Config globalConfig;

    private String domain;
    private CookieStore cookieStore;

    public Config(String domain) {
        this.domain = domain;
        this.cookieStore = new BasicCookieStore();
    }

    public Config(String domain, CookieStore cookieStore) {
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
     * @param global 是否设置为全局配置
     * @return config
     * @throws ConfigException 若全局配置非空时，抛出异常
     */
    public static Config buildGlobal(String domain, boolean global) throws ConfigException {
        Config config = new Config(domain, new BasicCookieStore());
        if (global) {
            if (globalConfig != null) {
                throw new ConfigException("global config not empty, danger operation");
            }
            globalConfig = config;
        }
        return config;
    }

    /**
     * init config
     *
     * @param domain 域名
     * @param global 是否设置为全局配置
     * @param cookieStore cookie存储
     * @return config
     * @throws ConfigException 若全局配置非空时，抛出异常
     */
    public Config buildGlobal(String domain, CookieStore cookieStore, boolean global) throws ConfigException {
        Config config = new Config(domain, cookieStore);
        if (global) {
            if (globalConfig != null) {
                throw new ConfigException("global config not empty, danger operation");
            }
            globalConfig = config;
        }
        return config;
    }

    /**
     * 获取全局配置类
     *
     * @return 全局配置
     */
    public static Config getGlobalConfig() {
        return globalConfig;
    }

    /**
     * clean global config
     */
    public static void clean() {
        globalConfig = null;
    }

}
