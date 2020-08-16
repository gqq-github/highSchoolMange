package cn.gq.highschoolmanger.entity;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import cn.gq.highschoolmanger.R;

public class userInfo<T extends AppCompatActivity>  {
    private String userId ;
    private String passWord  ;
    private  Integer role ;
    public userInfo () {

    }
    public userInfo (T activity) {
        this.userId = ((EditText) activity.findViewById(R.id.userId)).getText().toString();
        this.passWord = ((EditText) activity.findViewById(R.id.password)).getText().toString();
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
