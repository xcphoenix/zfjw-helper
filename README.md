# ZFJW-Helper
新正方教务系统

## 使用

```java
@Test
void testLogin() throws PublicKeyException, LoginException, IOException {
    // 设置用户信息
    User user = User.builder("学号", "密码");
    // 设置登录信息
    // 域名类似：www.zfjw.xupt.edu.cn，需符合 url 要求，且不能以 '/' 开头
    LoginService loginService = new LoginServiceImpl("教务系统域名", user);
    // 登录，并获取返回的登录状态信息
    LoginStatus loginStatus = loginService.login();
    // 若登录成功
    if (!loginStatus.isSuccess()) {
        // 获取登录成功的cookie
        CookieStore cookieStore = loginService.getCookie();
        // 设置cookie，并使用 HttpClient 去发送响应请求
        CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .build();
        HttpGet httpGet = new HttpGet("url");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(EntityUtils.toString(response.getEntity()));
    } else {
        // 登录失败：打印错误信息(教务系统网页上的tips)
        System.out.println(loginStatus.getErrorMsg());
    }
}
```

