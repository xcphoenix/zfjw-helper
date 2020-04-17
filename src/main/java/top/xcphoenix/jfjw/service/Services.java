package top.xcphoenix.jfjw.service;

import top.xcphoenix.jfjw.expection.ServiceException;
import top.xcphoenix.jfjw.global.Config;

import java.lang.reflect.Modifier;

/**
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
        if (!AbstractService.class.isAssignableFrom(clazz) ||
                Modifier.isAbstract(clazz.getModifiers())) {
            throw new ServiceException("class is invalid");
        }
        AbstractService abstractService;
        Config config = Config.getGlobalConfig();
        if (config == null) {
            throw new ServiceException("global config isn't be define");
        }
        try {
            abstractService = (AbstractService) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("class can't be create by default constructor");
        }
        abstractService.init(config.getDomain(), config.getCookieStore());
        return (T) abstractService;
    }

    /**
     * 使用指定配置创建服务
     *
     * @param clazz 服务具体类
     * @return 服务
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T create(Class<T> clazz, Config config) {
        if (!AbstractService.class.isAssignableFrom(clazz) ||
                Modifier.isAbstract(clazz.getModifiers())) {
            throw new ServiceException("class is invalid");
        }
        AbstractService abstractService;
        try {
            abstractService = (AbstractService) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("class can't be create by default constructor");
        }
        abstractService.init(config.getDomain(), config.getCookieStore());
        return (T) abstractService;
    }

}
