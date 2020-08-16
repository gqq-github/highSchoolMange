package cn.gq.highschoolmanger.student;

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
import cn.gq.highschoolmanger.entity.StudentCourseEntity;
import cn.gq.highschoolmanger.loading.LoadingX;

public class ShowStudentCourseActivity extends AppCompatActivity {
    private ArrayList <StudentCourseEntity> studentCourses = new ArrayList<>() ;
    private ListView lv_student_course  ;
    private MyAdapter myAdapter_student_select_course ;
    private Context context ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_show_course);
        context =  ShowStudentCourseActivity.this ;
        initListView();
    }
    public void initListView () {
        lv_student_course = findViewById(R.id.lv_student_course);
        myAdapter_student_select_course = new MyAdapter<StudentCourseEntity>(studentCourses,R.layout.student_course_item) {
            @Override
            public void bindView(final ViewHolder holder, StudentCourseEntity obj) {
                holder.setText(R.id.tv_student_course_name,obj.toString()) ;
                holder.setOnClickListener(R.id.tv_student_course_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowStudentCourseActivity.this, StudentQianDaoActivity.class);
                        intent.putExtra("studentCourse",studentCourses.get(holder.getItemPosition()));
                        startActivity(intent);
                        finish();
                    }
                }) ;
                if(obj.getShow()) {
                holder.setImageResource(R.id.iv_student_delete_icon,R.drawable.delete);
                holder.setOnClickListener(R.id.iv_student_delete_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"点击"+holder.getItemPosition(), Toast.LENGTH_SHORT).show();
                        doDeleteStudentCourse(holder.getItemPosition());
                    }
                });
                } else  {
                    holder.getView(R.id.iv_student_delete_icon).setVisibility(View.GONE);
                }
            }

            @Override
            public String requestPath() {
                return null;
            }
        } ;
        lv_student_course.setAdapter(myAdapter_student_select_course);
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
        baseRequest.doGet(null,"student/showstudentcourse");
    }
    public void  delJsonData (JSONArray jsonArray) {
        for (int i = 0 ; i<jsonArray.length();  i++ ) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                StudentCourseEntity studentCourseEntity = new StudentCourseEntity();
                studentCourseEntity.setId(jsonObject.getInt("Id"));
                studentCourseEntity.setCollegeName(jsonObject.getString("collegeName"));
                studentCourseEntity.setMajorName(jsonObject.getString("majorName"));
                studentCourseEntity.setCourseName(jsonObject.getString("courseName"));
                studentCourseEntity.setCourseType(jsonObject.getInt("courseType"));
                studentCourseEntity.setCourseCredit(jsonObject.getInt("courseCredit"));
                studentCourseEntity.setTeacherName(jsonObject.getString("teacherName"));
                studentCourseEntity.setShow(jsonObject.getInt("show")==1);
                studentCourseEntity.setMajorId(jsonObject.getInt("majorId"));
                studentCourseEntity.setCourseId(jsonObject.getString("courseId"));
                studentCourseEntity.setTeacherId(jsonObject.getString("teacherId"));
                myAdapter_student_select_course.add(studentCourseEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        myAdapter_student_select_course.notifyDataSetChanged();
    }

    public void doDeleteStudentCourse (final int position) {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据删除");
        loadingX.setMessage("数据删除中...");
        StudentCourseEntity studentCourseEntity = studentCourses.get(position);
        Integer id = studentCourseEntity.getId();
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                studentCourses.remove(position) ;
                myAdapter_student_select_course.notifyDataSetChanged();
            }
        };
        baseRequest.doGet(null,"student/deletestudentcourse/"+id);
    }

}
