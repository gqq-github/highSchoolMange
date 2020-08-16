package cn.gq.highschoolmanger.jiaoshi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;

public class TeacherSeeStudentSignIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spin_sign_status;
    private ListView lv_show_student_sign_status;
    private ArrayList<String> signStudents = new ArrayList<>();
    private MyAdapter<String> sign_adapter;
    private HashSet<String> isSign ;
    private HashSet<String> noSign ;
    private TextView tv_sign_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_see_student_signin);
        init();

    }

    public void init() {
        spin_sign_status = findViewById(R.id.spin_sign_status);
        lv_show_student_sign_status = findViewById(R.id.lv_show_student_sign_status);
        tv_sign_info = findViewById(R.id.tv_sign_info);
        sign_adapter = new MyAdapter<String>(signStudents, R.layout.lv_student_sign_item) {
            @Override
            public void bindView(ViewHolder holder, String obj) {
                holder.setText(R.id.tv_lv_sign_student, obj);
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        lv_show_student_sign_status.setAdapter(sign_adapter);
        spin_sign_status.setOnItemSelectedListener(this);
        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        String courseName = intent.getExtras().getString("courseName");
        String className = intent.getExtras().getString("className");
        Bundle sign = intent.getExtras().getBundle("sign");
        isSign = (HashSet<String>) sign.getSerializable("isSign");
        noSign = (HashSet<String>) sign.getSerializable("noSign");
        signStudents.addAll(isSign);
        tv_sign_info.setText("课程：" + courseName + "\n" + "班级" + className + "\n" + "总人数："
                + (isSign.size() + noSign.size()) + "\n已签到人数：" + isSign.size() + "\n未签到人数：" + noSign.size()
        );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        signStudents.clear();
        if(position==0) {

            signStudents.addAll(isSign);
        }else {
            signStudents.addAll(noSign);
        }
        sign_adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
