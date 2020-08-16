package cn.gq.highschoolmanger.jiaoshi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.TeacherSelectCourseEntity;
import cn.gq.highschoolmanger.loading.LoadingX;

public class ShowTeacherSelectCourseActivity extends AppCompatActivity {
   private ArrayList <TeacherSelectCourseEntity> teacherCourses = new ArrayList<>() ;
   private  ListView lv_teacher_course  ;
   private MyAdapter myAdapter_teacher_select_course ;
   private Context context ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_show_course);
        context = ShowTeacherSelectCourseActivity.this ;
        initListView();
    }
    public void initListView () {
        lv_teacher_course = findViewById(R.id.lv_teacher_course);
        myAdapter_teacher_select_course = new MyAdapter<TeacherSelectCourseEntity>(teacherCourses,R.layout.teacher_course_item) {
            @Override
            public void bindView(final ViewHolder holder, TeacherSelectCourseEntity obj) {
                holder.setText(R.id.tv_teacher_course_name,obj.toString()) ;
                holder.setImageResource(R.id.iv_teacher_delete_icon,R.drawable.delete);
                holder.setOnClickListener(R.id.iv_teacher_delete_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"点击"+holder.getItemPosition(), Toast.LENGTH_SHORT).show();
                      doDeleteTeacherCourse(holder.getItemPosition());
                    }
                });
                // 点击课程的时候跳转到相应的课程签到页面
                holder.setOnClickListener(R.id.tv_teacher_course_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowTeacherSelectCourseActivity.this,CreateSignInActivity.class);

                        intent.putExtra("teacherCourse",teacherCourses.get(holder.getItemPosition()));
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public String requestPath() {
                return null;
            }
        } ;
        lv_teacher_course.setAdapter(myAdapter_teacher_select_course);
        loadData();
    }
    public void loadData () {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据加载");
        loadingX.setMessage("加载中...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    delJsonData(data.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        baseRequest.doGet(null,"teacher/showteachercourse");
    }
    public void  delJsonData (JSONArray jsonArray) {
        for (int i = 0 ; i<jsonArray.length();  i++ ) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                TeacherSelectCourseEntity teacherCourse = new TeacherSelectCourseEntity();
                teacherCourse.setId(jsonObject.getInt("id"));
                teacherCourse.setCollegeName(jsonObject.getString("collegeName"));
                teacherCourse.setMajorName(jsonObject.getString("majorName"));
                teacherCourse.setCourseType(jsonObject.getInt("courseType"));
                teacherCourse.setCourseName(jsonObject.getString("courseName"));
                teacherCourse.setCourseCredit(jsonObject.getInt("courseCredit"));
                 teacherCourse.setMajorId(jsonObject.getInt("majorId"));
                 teacherCourse.setCourseId(jsonObject.getString("courseId"));
                teacherCourses.add(teacherCourse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        myAdapter_teacher_select_course.notifyDataSetChanged();
    }

    public void doDeleteTeacherCourse (final int position) {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据删除");
        loadingX.setMessage("数据删除中...");
        TeacherSelectCourseEntity teacherSelectCourseEntity = teacherCourses.get(position);
        Integer id = teacherSelectCourseEntity.getId();
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                teacherCourses.remove(position) ;
                myAdapter_teacher_select_course.notifyDataSetChanged();
            }
        };
        baseRequest.doGet(null,"teacher/deleteteachercourse/"+id);
    }

}
