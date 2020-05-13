# ZFJW-Helper
新正方教务系统

## 使用

示例代码：

```java
    @Test
    void demo() throws ConfigException, LoginException {
        User user = new User(code, password);
        ServiceConfig.buildGlobal("www.zfjw.xupt.edu.cn/");
        LoginService service = ServiceBuilder.create(LoginServiceImpl.class);
        LoginStatus status = service.login(user);
        if (status.isSuccess()) {
            // user info
            UserBaseInfo userBaseInfo = ServiceBuilder.create(UserInfoServiceImpl.class)
                    .getUserInfo();
            log.info(userBaseInfo);

            // course list
            List<Course> list = ServiceBuilder.create(ClassTableServiceImpl.class)
                    .getCourses(2018, 1);
            log.info(list);
        } else {
            log.warn(status);
        }
    }
```
> 可以通过 BaseService#getContent() 获取 cookie 信息，在登录后执行自定义的逻辑处理。 