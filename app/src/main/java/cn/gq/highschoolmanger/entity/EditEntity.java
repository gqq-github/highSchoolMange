package cn.gq.highschoolmanger.entity;

public class EditEntity {
    public String name  ;
    public String editName;
    public String requestName;
    public String responseValue;

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }

    public EditEntity(String name, String editName, String requestName, String responseValue) {
        this.name = name;
        this.editName = editName;
        this.requestName = requestName;
        this.responseValue = responseValue;
    }

    public EditEntity(String name, String editName, String requestName) {
        this.name = name;
        this.editName = editName;
        this.requestName = requestName ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditName() {
        return editName;
    }

    public void setEditName(String editName) {
        this.editName = editName;
    }
}
