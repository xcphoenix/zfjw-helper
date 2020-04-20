package top.xcphoenix.jfjw.service.core;

import top.xcphoenix.jfjw.config.CourseConfig;
import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.model.course.ClassTable;
import top.xcphoenix.jfjw.model.course.Course;

import java.io.File;
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

    /**
     * 课程数据转换
     *
     * @param courses 课程信息
     * @return 课程表格式
     */
    ClassTable convert(List<Course> courses);

    /**
     * 导出课表至 csv 中
     *
     * @param file 文件路径
     * @param table 课程表
     * @param courseConfig 课程表配置
     */
    void exportCsv(File file, ClassTable table, CourseConfig courseConfig);

}
