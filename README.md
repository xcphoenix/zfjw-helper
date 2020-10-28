# ZFJW-Helper
新正方教务系统

## 使用

示例代码：

```java
    String code;
    String password;

    @BeforeEach
    void setProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(properties.getClass().getResourceAsStream("/user.properties"));
        code = properties.getProperty("code");
        password = properties.getProperty("password");
    }

    @Test
    void demo() throws LoginException {
        User user = new User(code, password);
        ServiceConfig.buildGlobal("www.zfjw.xupt.edu.cn/");
        LoginStatus status = new LoginServiceImpl().login(user);
        if (status.isSuccess()) {
            // user info
            UserInfoService userInfoService = new UserInfoServiceImpl();
            UserBaseInfo userBaseInfo = userInfoService.getUserInfo();
            log.info(userBaseInfo);

            // course list
            List<Course> list = new ClassTableServiceImpl().getCourses(2020, 1);
            log.info(list);
        } else {
            log.warn("login failed, error msg: " + status.getErrorMsg());
        }
    }
```
