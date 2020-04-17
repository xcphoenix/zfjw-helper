import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import top.xcphoenix.jfjw.expection.ConfigException;
import top.xcphoenix.jfjw.expection.LoginException;
import top.xcphoenix.jfjw.expection.PublicKeyException;
import top.xcphoenix.jfjw.global.Config;
import top.xcphoenix.jfjw.model.User;
import top.xcphoenix.jfjw.service.Services;
import top.xcphoenix.jfjw.service.core.LoginService;
import top.xcphoenix.jfjw.service.core.impl.LoginServiceImpl;

/**
 * @author      xuanc
 * @date        2020/4/17 上午11:29
 * @version     1.0
 */ 
public class DemoTest {

    @Test
    void demo() throws PublicKeyException, LoginException, ConfigException {
        User user = new User("*****", "******");
        Config.buildGlobal("www.zfjw.xupt.edu.cn/", true);
        LoginService service = Services.create(LoginServiceImpl.class);
        service.login(user);
        System.out.println(service.getLoginStatus());
    }

    @Test
    void test() {
        String json = "{\"hello\": 1}";
        Document document = Jsoup.parse(json);
        System.out.println(document);
    }


}
