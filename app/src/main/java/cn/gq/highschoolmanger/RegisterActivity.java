package cn.gq.highschoolmanger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.gq.highschoolmanger.listener.SelectButtonListener;

public class RegisterActivity extends AppCompatActivity {
    private static String TAG = "REGISTERACTIVITY";
    private String password;
    private String rePassword;
    private String userName;
    private int role;
    private static Context context = null;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        context = getApplicationContext();
        // System.out.println(this);
        // userInfo userInfo = new userInfo(this);
        SelectButtonListener selectButtonListener = new SelectButtonListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.selectGroup);
        radioGroup.setOnCheckedChangeListener(selectButtonListener);
        View registerView = this.findViewById(R.id.main_btn_register);
        registerView.setOnClickListener(selectButtonListener);

    }

}
