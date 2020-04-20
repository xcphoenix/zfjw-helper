package top.xcphoenix.jfjw.model.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author      xuanc
 * @date        2020/4/13 下午10:02
 * @version     1.0
 */
@Getter
@Setter
public class User {
    
    private String stuCode;
    private String password;
    
    public User(@NonNull String stuCode, @NonNull String password) {
        this.stuCode = stuCode;
        this.password = password;
    }

}
