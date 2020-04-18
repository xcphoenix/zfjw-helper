package top.xcphoenix.jfjw.service;

import top.xcphoenix.jfjw.expection.ServiceException;
import top.xcphoenix.jfjw.global.Config;

import java.lang.reflect.Modifier;

/**
 * TODO AOP 处理服务
 *
 * 配合config创建内置服务
 *
 * @author      xuanc
 * @date        2020/4/17 上午10:48
 * @version     1.0
 */ 
public class Services {

    /**
     * 使用全局配置创建服务
     *
     * @param clazz 服务具体类
     * @return 服务
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T create(Class<T> clazz) {
        if (!BaseService.class.isAssignableFrom(clazz) ||
                Modifier.isAbstract(clazz.getModifiers())) {
            throw new ServiceException("class is invalid");
        }
        BaseService baseService;
        Config config = Config.getGlobalConfig();
        if (config == null) {
            throw new ServiceException("global config isn't be define");
        }
        try {
            baseService = (BaseService) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("class can't be create by default constructor");
        }
        baseService.init(config.getDomain(), config.getCookieStore());
        return (T) baseService;
    }

    /**
     * 使用指定配置创建服务
     *
     * @param clazz 服务具体类
     * @return 服务
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T create(Class<T> clazz, Config config) {
        if (!BaseService.class.isAssignableFrom(clazz) ||
                Modifier.isAbstract(clazz.getModifiers())) {
            throw new ServiceException("class is invalid");
        }
        BaseService baseService;
        try {
            baseService = (BaseService) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("class can't be create by default constructor");
        }
        baseService.init(config.getDomain(), config.getCookieStore());
        return (T) baseService;
    }

}
