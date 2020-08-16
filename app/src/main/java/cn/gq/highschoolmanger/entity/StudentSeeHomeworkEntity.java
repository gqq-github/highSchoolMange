package cn.gq.highschoolmanger.entity;

import java.io.Serializable;

public class StudentSeeHomeworkEntity implements Serializable {
    private String className ;
    private String question ;
    private String teacherPicture;
    private String credit ;

    public StudentSeeHomeworkEntity(String className, String question, String teacherPicture, String credit) {
        this.className = className;
        this.question = question;
        this.teacherPicture = teacherPicture;
        this.credit = credit;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTeacherPicture() {
        return teacherPicture;
    }

    public void setTeacherPicture(String teacherPicture) {
        this.teacherPicture = teacherPicture;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
