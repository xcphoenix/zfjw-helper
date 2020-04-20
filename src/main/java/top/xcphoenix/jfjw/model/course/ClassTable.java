package top.xcphoenix.jfjw.model.course;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2020/4/18 下午4:01
 * @version     1.0
 */
@Data
public class ClassTable {

    /**
     * 一周中最晚的课
     */
    private int dayMaxCourse;

    Map<DayOfWeek, List<Course>> courses;
    Map<Integer, Course> courseDetail;

}
