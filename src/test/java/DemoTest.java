import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.xcphoenix.jfjw.expection.ConfigException;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.config.ServiceConfig;
import top.xcphoenix.jfjw.model.course.Course;
import top.xcphoenix.jfjw.model.login.LoginStatus;
import top.xcphoenix.jfjw.model.user.User;
import top.xcphoenix.jfjw.model.user.UserBaseInfo;
import top.xcphoenix.jfjw.service.Services;
import top.xcphoenix.jfjw.service.core.LoginService;
import top.xcphoenix.jfjw.service.core.impl.ClassTableServiceImpl;
import top.xcphoenix.jfjw.service.core.impl.LoginServiceImpl;
import top.xcphoenix.jfjw.service.core.impl.UserInfoServiceImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author      xuanc
 * @date        2020/4/17 上午11:29
 * @version     1.0
 */
@Log4j
public class DemoTest {

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
    void demo() throws ConfigException, LoginException, URISyntaxException {
        User user = new User(code, password);
        ServiceConfig.buildGlobal("www.zfjw.xupt.edu.cn/", true);
        LoginService service = Services.create(LoginServiceImpl.class);
        LoginStatus status = service.login(user);
        if (status.isSuccess()) {
            // try {
            //     UserBaseInfo userBaseInfo = Services.create(UserInfoServiceImpl.class)
            //             .getUserInfo();
            //     log.info(userBaseInfo);
            // } catch (LoginException e) {
            //     log.warn("未登录！");
            // }
            List<Course> list = Services.create(ClassTableServiceImpl.class)
                    .getCourses(2018, 1);
            log.info(list);
        } else {
            log.warn(status);
        }
    }

}
