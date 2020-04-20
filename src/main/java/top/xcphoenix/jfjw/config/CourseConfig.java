package top.xcphoenix.jfjw.config;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author      xuanc
 * @date        2020/4/19 下午6:04
 * @version     1.0
 */
@Data
public class CourseConfig {

    private LocalDate startWeekDate;
    private List<CourseTime> coursesTime;

    /**
     * 持续时间
     */
    private int defaultTime = -1;

    private static final List<CourseTime> DEFAULT_COURSE_TIMES = new ArrayList<>();
    static {
        int defaultClassTime = 50;
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(8, 0), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(8, 55), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(10, 15), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(11, 5), defaultClassTime));

        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(14, 30), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(15, 25), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(16, 35), defaultClassTime));
        DEFAULT_COURSE_TIMES.add(new CourseTime(LocalTime.of(17, 25), defaultClassTime));
    }

    /**
     * @throws RuntimeException defaultTime < 0
     */
    public void setDefaultTime(int defaultTime) {
        if (defaultTime <= 0) {
            throw new RuntimeException("time can't <= 0");
        }
        this.defaultTime = defaultTime;
    }

    /** Build instance */

    private CourseConfig(LocalDate startWeekDate, List<CourseTime> coursesTime) {
        this.startWeekDate = startWeekDate;
        this.coursesTime = coursesTime;
    }

    public static CourseConfig build(Date date) {
        return new CourseConfig(getDateWeekMonday(date), DEFAULT_COURSE_TIMES);
    }

    public static CourseConfig build(Date date, List<CourseTime> coursesTime) {
        return new CourseConfig(getDateWeekMonday(date), coursesTime);
    }

    /** Methods */

    public CourseConfig addRule(int hour, int minute) {
        if (defaultTime == -1) {
            throw new RuntimeException("invalid course time");
        }
        return addRule(hour, minute, defaultTime);
    }

    public CourseConfig addRule(int hour, int minute, int time) {
        this.coursesTime.add(new CourseTime(LocalTime.of(hour, minute), time));
        return this;
    }

    public CourseConfig addCustomRule(int index, int hour, int minute) {
        if (defaultTime == -1) {
            throw new RuntimeException("invalid course time");
        }
        return addCustomRule(index, hour, minute, defaultTime);
    }

    public CourseConfig addCustomRule(int index, int hour, int minute, int time) {
        this.coursesTime.add(index, new CourseTime(LocalTime.of(hour, minute), time));
        return this;
    }

    private static LocalDate getDateWeekMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        return LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    /** Inner class */

    public static class CourseTime {
        LocalTime start;
        int time;

        public CourseTime(LocalTime start, int time) {
            this.start = start;
            this.time = time;
        }
    }

}
