package top.xcphoenix.jfjw.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import top.xcphoenix.jfjw.config.CourseConfig;
import top.xcphoenix.jfjw.expection.NotLoggedInException;
import top.xcphoenix.jfjw.expection.ServiceException;
import top.xcphoenix.jfjw.model.course.ClassTable;
import top.xcphoenix.jfjw.model.course.Course;
import top.xcphoenix.jfjw.model.course.CourseTpMeta;
import top.xcphoenix.jfjw.service.BaseService;
import top.xcphoenix.jfjw.service.core.ClassTableService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.http.Consts.UTF_8;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/4/20 上午10:03
 */
public class ClassTableServiceImpl extends BaseService implements ClassTableService {

    private static Map<Integer, Integer> periodMap = new HashMap<>(2);

    static {
        periodMap.put(1, 3);
        periodMap.put(2, 12);
    }

    @Override
    public List<Course> getCourses() throws NotLoggedInException {
        int[] data = getNowPeriod();
        return getCourses(data[0], data[1]);
    }

    @Override
    public List<Course> getCourses(int year, int num) throws NotLoggedInException {
        try (CloseableHttpResponse response = sendReq(year, getStudyPeriod(num))) {
            if (super.isNeedLogin(response)) {
                throw new NotLoggedInException("need login");
            }

            return dealWithResp(response);
        } catch (IOException e) {
            throw new ServiceException("io error");
        }
    }

    @Override
    public ClassTable convert(List<Course> courses) {
        return null;
    }

    @Override
    public void exportCsv(File file, ClassTable table, CourseConfig courseConfig) {

    }

    public static void setPeriodMap(Map<Integer, Integer> periodMap) {
        ClassTableServiceImpl.periodMap = periodMap;
    }

    private CloseableHttpResponse sendReq(int year, int period) {
        HttpPost httpPost;

        try {
            httpPost = new HttpPost(urlManager.getClassTableApiLink(this.domain).build());
            httpPost.setEntity(new UrlEncodedFormEntity(convertYearPeriod(year, period), UTF_8));
        } catch (URISyntaxException e) {
            throw new ServiceException("get classTable api url is invalid");
        }

        try {
            return httpClient.execute(httpPost, context);
        } catch (IOException e) {
            throw new ServiceException("io error");
        }
    }

    private List<Course> dealWithResp(CloseableHttpResponse response) throws NotLoggedInException {
        if (super.isNeedLogin(response)) {
            throw new NotLoggedInException("need login");
        }

        JSONArray courseArray;
        Map<String, Course> idMapCourse = new HashMap<>();

        try {
            courseArray = JSON.parseObject(EntityUtils.toString(response.getEntity()))
                    .getJSONArray("kbList");
        } catch (IOException e) {
            throw new ServiceException("io error");
        }

        if (courseArray == null) {
            throw new ServiceException("classTable not found");
        }
        for (int i = 0; i < courseArray.size(); i++) {
            JSONObject obj = courseArray.getJSONObject(i);
            String cName = obj.getString("kcmc");
            Course course = idMapCourse.get(cName);
            if (course == null) {
                course = new Course();
                course.setCourseName(cName);
                course.setTeacher(obj.getString("xm"));
                course.setScore(Double.parseDouble(obj.getString("xf")));
                course.setAccessKind(obj.getString("khfsmc"));
                course.setCompositionOfTime(obj.getString("kcxszc"));
                course.setWeekTime(Integer.parseInt(obj.getString("zhxs")));
                course.setAllTime(Integer.parseInt(obj.getString("zxs")));
                course.setCourseTpMetas(new ArrayList<>());
            }
            List<CourseTpMeta> metas = course.getCourseTpMetas();
            CourseTpMeta meta = new CourseTpMeta();
            dealMetaWeek(meta, obj.getString("zcd"));
            meta.setWeek(DayOfWeek.of(Integer.parseInt(obj.getString("xqj"))));
            dealMetaPeriod(meta, obj.getString("jcs"));
            meta.setPosition(String.join(" ", obj.getString("xqmc"), obj.getString("cdmc")));

            metas.add(meta);
            idMapCourse.put(cName, course);
        }
        return new ArrayList<>(idMapCourse.values());
    }

    private void dealMetaPeriod(CourseTpMeta meta, String str) {
        String[] items = str.split("-");
        meta.setStartPeriod(Integer.parseInt(items[0]));
        if (items.length > 1) {
            meta.setEndPeriod(Integer.parseInt(items[1]));
        } else {
            meta.setEndPeriod(meta.getStartPeriod());
        }
    }

    private void dealMetaWeek(CourseTpMeta meta, String str) {
        String regex = "(\\d+)-(\\d+)周(\\((.)\\))?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            if (matcher.groupCount() >= 2) {
                meta.setStartWeek(Integer.parseInt(matcher.group(1)));
                meta.setEndWeek(Integer.parseInt(matcher.group(2)));
                if (matcher.group(4) != null) {
                    meta.setWeekKind(CourseTpMeta.WeekKind.parse(matcher.group(4)));
                }
                return;
            }
        }
        throw new ServiceException("parse course week data error");
    }

    private static List<NameValuePair> convertYearPeriod(int year, int period) {
        String yearParamName = "xnm";
        String periodParamName = "xqm";

        List<NameValuePair> list = new ArrayList<>(2);
        list.add(new BasicNameValuePair(yearParamName, String.valueOf(year)));
        list.add(new BasicNameValuePair(periodParamName, String.valueOf(period)));

        return list;
    }

    private static int getStudyPeriod(int val) {
        if (val > 0 && val < 3 && periodMap.containsKey(val)) {
            return periodMap.get(val);
        }
        /*
         * 非范围内值直接返回
         */
        return val;
    }

    private int[] getNowPeriod() throws ServiceException, NotLoggedInException {
        String yearId = "xnm";
        String periodId = "xqm";
        int[] res = new int[2];

        HttpGet httpGet;
        try {
            httpGet = new HttpGet(urlManager.getClassTableSimplePage(this.domain).build());
        } catch (URISyntaxException e) {
            throw new ServiceException("get classTable simple page url is invalid");
        }
        try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {
            if (super.isNeedLogin(response)) {
                throw new NotLoggedInException("need login");
            }

            Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));

            Elements elements = document.getElementById(yearId).getElementsByTag("option");
            res[0] = Integer.parseInt(
                    elements.stream().filter(e -> "selected".equals(e.attr("selected")))
                            .collect(Collectors.toList())
                            .get(0).attr("value")
            );

            elements = document.getElementById(periodId).getElementsByTag("option");
            res[1] = Integer.parseInt(
                    elements.stream().filter(e -> "selected".equals(e.attr("selected")))
                            .collect(Collectors.toList())
                            .get(0).attr("value")
            );

        } catch (IOException e) {
            throw new ServiceException("io error");
        }
        return res;
    }


}
