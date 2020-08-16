package cn.gq.highschoolmanger.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.SendHomeWork;
import cn.gq.highschoolmanger.loading.LoadingX;

public class StudentSendHomeWorkActivity extends AppCompatActivity {
    private ListView  lv_show_homework ;
    private MyAdapter lv_show_homework_adapter ;
    private ArrayList<SendHomeWork> homeWorks = new ArrayList<>();
    private TextView homeworker_btn_answer ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_show_homework);
        init () ;
    }
    public void init () {
        lv_show_homework = findViewById(R.id .lv_show_homework);
        homeworker_btn_answer = findViewById(R.id.homeworker_btn_answer);
        lv_show_homework_adapter = new MyAdapter <SendHomeWork>(homeWorks,R.layout.homework_item) {
            @Override
            public void bindView(final ViewHolder holder, SendHomeWork obj) {
                holder.setText(R.id.tv_homework, showTextView(obj.getCourseName(),obj.getQuestion(),obj.getTeacherName(),obj.getCourseSendTime()));
                if(obj.getShow()==1) {
                    // 表示已经完成过答题
                    holder.setVisibility(R.id.homeworker_btn_answer,View.GONE);
                }else  {
                   holder.setVisibility(R.id.homeworker_btn_answer,View.VISIBLE);
                }
                holder.setOnClickListener(R.id.homeworker_btn_answer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackgroundResource(R.drawable.text_bg_onpress);
                        Intent intent = new Intent(StudentSendHomeWorkActivity.this,DoHomeworkByCamera.class);
                        intent.putExtra("homework",homeWorks.get(holder.getItemPosition()));
                        startActivity(intent);
                        finish();
                    }
                }) ;
            }

            @Override
            public String requestPath() {
                return null;
            }

            public String showTextView (String courseName,String question,String teacherName,Long lastTime) {
                String res  ;
                Date date = new Date() ;
                date.setTime(lastTime);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String format1 = format.format(date);
               res = "课程名字："+courseName+"\n"+"作业："+question+"\n" +"教师："+teacherName
                       +"\n" +"提交时间："+ format1 ;
               return res ;
            }
        };
        lv_show_homework.setAdapter(lv_show_homework_adapter);
        loadData () ;
    }
    public void loadData () {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("加载...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    delJson(data.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        baseRequest.doGet(null,"student/showHomework");
    }
    public void delJson (JSONArray  jsonArray) throws JSONException {
        for (int i =0 ; i< jsonArray.length() ; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            SendHomeWork homeWork = new SendHomeWork();
            homeWork.setId(jsonObject.getInt("id"));
            homeWork.setTeacherId(jsonObject.getString("teacherId"));
            homeWork.setTeacherName(jsonObject.getString("teacherName"));
            homeWork.setCourseId(jsonObject.getString("courseId"));
            homeWork.setCourseName(jsonObject.getString("courseName"));
            homeWork.setQuestion(jsonObject.getString("question"));
            homeWork.setCourseSendTime(jsonObject.getLong("courseSendTime"));
            homeWork.setClassName(jsonObject.getString("className"));
            homeWork.setMajorId(jsonObject.getInt("majorId"));
            homeWork.setMajorName(jsonObject.getString("majorName"));
            homeWork.setShow(jsonObject.getInt("show"));
            lv_show_homework_adapter.add(homeWork);
        }
    }


}
