package cn.gq.highschoolmanger.entity;

import androidx.annotation.NonNull;

public class Course {
    public String courseName ;
    public String courseStartTime ;
    public String courseEndTime ;
    public String classLoc;
    public String teacherName;
    Boolean flag = false ;

    @NonNull
    @Override
    public String toString() {
        if(flag){
            return "今日没有课程";
        }
        return courseStartTime + "--"+courseEndTime + " "
                + courseName + " " + teacherName + " " + classLoc;
    }
    public Course () {}  ;
    public Course(Boolean flag) {
        this.flag = flag ;
    }
}
