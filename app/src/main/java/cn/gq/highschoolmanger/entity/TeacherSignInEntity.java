package cn.gq.highschoolmanger.entity;

import java.io.Serializable;

public class TeacherSignInEntity implements Serializable {
    private Integer id ;
    private Integer majorId ;
    private String courseId ;
    private String courseName ;
    private Long endTime ;
    private String verifed; // 验证IP
    private Integer keepTime ;
    private String teacherId  ;
    private Integer tip ;
    private String className ;

    public TeacherSignInEntity (TeacherSelectCourseEntity entity)  {
      this.majorId = entity.getMajorId() ;
      this.courseId = entity.getCourseId();
      this.courseName = entity.getCourseName() ;
    }

    public TeacherSignInEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
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

    public Long getEndTime() {
        return System.currentTimeMillis()+keepTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getVerifed() {
        return verifed;
    }

    public void setVerifed(String verifed) {
        this.verifed = verifed;
    }

    public Integer getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(Integer keepTime) {
        this.keepTime = keepTime*1000*60;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
