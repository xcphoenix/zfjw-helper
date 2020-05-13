package top.xcphoenix.jfjw.service.core;

import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.model.course.Course;

import java.util.List;

/**
 * @author      xuanc
 * @date        2020/4/18 下午4:00
 * @version     1.0
 */ 
public interface ClassTableService {

    /**
     * 获取当前课程信息
     *
     * @return 课程信息
     */
    List<Course> getCourses() throws NotLoggedInException;

    /**
     * 获取指定学年、学期课程信息
     *
     * @param year 学年
     * @param num 学期
     * @return 课程信息
     */
    List<Course> getCourses(int year, int num) throws NotLoggedInException;

}
