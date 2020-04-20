package top.xcphoenix.jfjw.model.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author      xuanc
 * @date        2020/4/17 上午10:07
 * @version     1.0
 */
@Data
public class UserBaseInfo {

    private String stuCode;
    private String name;
    private String sex;
    private String avatar = null;
    /** 籍贯 */
    private String homeTown;

    private int grade;
    private String iClass;
    private String major;
    private String faculty;
    private String school;

    /** 本科 */
    private String kind;

    /** 存储所有数据 */
    private Map<String, Object> allData;

}
