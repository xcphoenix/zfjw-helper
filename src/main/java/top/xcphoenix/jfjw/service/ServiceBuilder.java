package top.xcphoenix.jfjw.service;

import top.xcphoenix.jfjw.config.ServiceConfig;
import top.xcphoenix.jfjw.expection.ServiceException;

import java.lang.reflect.Modifier;

/**
 *
 * 配合config创建内置服务
 *
 * @author      xuanc
 * @date        2020/4/17 上午10:48
 * @version     1.0
 */
public class ServiceBuilder {

    private final ServiceConfig config;

    public ServiceBuilder(ServiceConfig config) {
        this.config = config;
    }

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
        ServiceConfig serviceConfig = ServiceConfig.getGlobalServiceConfig();
        if (serviceConfig == null) {
            throw new ServiceException("config serviceConfig isn't be define");
        }
        try {
            baseService = (BaseService) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("class can't be create by default constructor");
        }
        return (T) baseService.init(serviceConfig);
    }

    /**
     * 使用指定配置创建服务
     *
     * @param clazz 服务具体类
     * @return 服务
     */
    @SuppressWarnings({"unchecked"})
    public <T> T build(Class<T> clazz) {
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
        return (T) baseService.init(config);
    }

}