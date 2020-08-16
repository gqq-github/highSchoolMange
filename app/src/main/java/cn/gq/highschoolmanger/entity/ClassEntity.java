package cn.gq.highschoolmanger.entity;

import java.io.Serializable;

public class ClassEntity implements Serializable {
    private  Integer classId ;
    private String className ;

    public ClassEntity(String className) {
     this.className = className ;
    }
   public ClassEntity () {}
    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
