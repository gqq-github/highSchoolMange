package cn.gq.highschoolmanger.entity;

import android.graphics.Bitmap;

// 表示改作业被老师批改
public class StudentHomeworkTeacher {
    private  Integer answerId ;
    private String courseName ;
    private String studentId ;
    private String studentName ;
    private String question ;
    private String answerImage;
    private String className ;
    private EditEntity editEntity = new EditEntity ("分数：","请输入分数","","");
    private Bitmap bitmap ;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerImage() {
        return answerImage;
    }

    public void setAnswerImage(String answerImage) {
        this.answerImage = answerImage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public EditEntity getEditEntity() {
        return editEntity;
    }

    public void setEditEntity(EditEntity editEntity) {
        this.editEntity = editEntity;
    }
}
