package cn.gq.highschoolmanger.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TeacherSelectCourseEntity implements Serializable {
    private Integer id ;
    private String courseId ;
    private Integer majorId  ;
    private String collegeName ;
    private String majorName ;
    private String courseName ;
    private Integer courseType ;
    private Integer courseCredit;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

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
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer  getCourseCredit() {
      return courseCredit ;
    }

    public void setCourseCredit(Integer courseCredit) {
        this.courseCredit = courseCredit;
    }

    @NonNull
    @Override
    public String toString() {

        return "学院："+getCollegeName() + "\n" +"专业：" +getMajorName() +"\n" + "课程：" + getCourseName()
                +"\n" +"学分：" +getCourseCredit() +"\n" +"课程类型："+getCourseType();
    }
}
