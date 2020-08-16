package cn.gq.highschoolmanger.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class StudentCourseEntity implements Serializable {
    private  Integer Id ;
    private String collegeName ;
    private String majorName ;
    private Integer majorId ;
    private String courseName ;
    private  String courseId ;
    private Integer courseType ;
    private Integer courseCredit ;
    private  String teacherName  ;
    private String  teacherId ;
    private  Boolean show ; // 1 显示 ， 不显示

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseType() {
        String res = "" ;
        switch (courseType) {
            case 0:
                res = "选修" ;
                break;
            case 1 :
                res = "必选";
                break;
            case 2:
                res= "必修" ;
                break;
        }

        return res;
    }

    public void setCourseType(Integer courseType) {
        this.courseType = courseType;
    }

    public Integer getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(Integer courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    @NonNull
    @Override
    public String toString() {
          return "学院："+getCollegeName() + "\n" +"专业：" +getMajorName() +"\n" + "课程：" + getCourseName()
                +"\n" +"学分：" +getCourseCredit() +"\n" +"课程类型："+getCourseType()+"\n" +"老师：" + getTeacherName();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
