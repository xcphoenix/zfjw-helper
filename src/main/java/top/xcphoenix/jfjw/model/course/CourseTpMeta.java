package top.xcphoenix.jfjw.model.course;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * 课程时间、地点描述
 *
 * @author      xuanc
 * @date        2020/4/19 上午11:53
 * @version     1.0
 */
@Data
public class CourseTpMeta {

    private WeekKind weekKind = WeekKind.NORMAL;
    private int startWeek;
    private int endWeek;

    private DayOfWeek week;
    private int startPeriod;
    private int endPeriod;

    /** build from <code>CourseConfig</code> */
    private LocalTime startTime;
    private LocalTime endTime;

    private String position;

    public enum WeekKind {

        /**
         * 正常
         */
        NORMAL,
        /**
         * 单周
         */
        SINGLE_WEEK,
        /**
         * 双周
         */
        DOUBLE_WEEK;

        public static WeekKind parse(String str) {
            String singleTag = "单";
            String doubleTag = "双";

            if (singleTag.equals(str)) {
                return SINGLE_WEEK;
            } else if (doubleTag.equals(str)){
                return DOUBLE_WEEK;
            }
            throw new RuntimeException("invalid str");
        }

    }

}
