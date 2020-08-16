package cn.gq.highschoolmanger.entity;

import java.io.Serializable;

/**
 * @author DELL
 * @create 2020/5/7 19:59
 * @update 2020/5/7
 * Description: 用来封装教师发布作业
 * * @since 1.0.0
 */
public class SendHomeWork implements Serializable {
    private Integer id;
    private String teacherId;
    public String teacherName;
    private String courseId;
    private String courseName;
    private String question;
    private Integer courseTime; // 完成作业的时间（小时）
    private Long courseSendTime; // 最后提交作业的日期
    private String className; // 班级的名称
    private Integer majorId;
    private String majorName;
    private Integer credit;
    private Integer show ;

    public Integer getShow() {
        return show;
    }

    public void setShow(Integer show) {
        this.show = show;
    }

    public SendHomeWork() {

    }

    public SendHomeWork(String courseId, String courseName, String question, Integer courseTime, Long courseSendTime, String className, Integer majorId, String majorName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.question = question;
        this.courseTime = courseTime;
        this.courseSendTime = courseSendTime;
        this.className = className;
        this.majorId = majorId;
        this.majorName = majorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(Integer courseTime) {
        this.courseTime = courseTime;
    }

    public Long getCourseSendTime() {
        return courseSendTime;
    }

    public void setCourseSendTime(Long courseSendTime) {
        this.courseSendTime = courseSendTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
