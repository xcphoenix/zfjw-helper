package top.xcphoenix.jfjw.model.course;

import lombok.Data;

import java.util.List;

/**
 * 课程
 *
 * @author      xuanc
 * @date        2020/4/19 上午11:43
 * @version     1.0
 */
@Data
public class Course {

    private int courseId;
    private String courseName;
    private String teacher;
    private double score;

    /** 考核方式 */
    private String accessKind;

    /** 学时组成 */
    private String compositionOfTime;
    private int weekTime;
    private int allTime;

    private List<CourseTpMeta> courseTpMetas;

}
