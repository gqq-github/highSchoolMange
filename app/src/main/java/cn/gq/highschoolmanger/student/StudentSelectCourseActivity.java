package cn.gq.highschoolmanger.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
import cn.gq.highschoolmanger.utils.HeaderUtil;

public class StudentSelectCourseActivity extends AppCompatActivity {
    private ListView lv_student_select ;
    private ArrayList<StudentCourseEntity> courseEntities = new ArrayList<>() ;
    private MyAdapter studentCourse_adapter ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_select_course);
        init () ;
    }
    public void init () {
        initStudentSelectCourseListView();
    }
    public void initStudentSelectCourseListView () {
        lv_student_select = findViewById(R.id.lv_student_select_course);
        studentCourse_adapter = new MyAdapter<StudentCourseEntity>(courseEntities,R.layout.student_course_item) {
            @Override
            public void bindView(final ViewHolder holder, StudentCourseEntity obj) {
                 holder.setText(R.id.tv_student_course_name,obj.toString());
                 holder.setImageResource(R.id.iv_student_delete_icon,R.drawable.add);
                 holder.setOnClickListener(R.id.iv_student_delete_icon, new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                       doSaveSelectCourse(holder.getItemPosition());
                     }
                 }) ;
            }

            @Override
            public String requestPath() {
                return null;
            }
        } ;
        lv_student_select.setAdapter(studentCourse_adapter);
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
        baseRequest.doGet(null,"student/showselectcourse");
    }
    public void  delJsonData (JSONArray jsonArray) {
        for (int i = 0 ; i<jsonArray.length();  i++ ) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                StudentCourseEntity studentCourseEntity = new StudentCourseEntity();
                studentCourseEntity.setCollegeName(jsonObject.getString("collegeName"));
                studentCourseEntity.setMajorName(jsonObject.getString("majorName"));
                studentCourseEntity.setCourseName(jsonObject.getString("courseName"));
                studentCourseEntity.setCourseId(jsonObject.getString("courseId"));
                studentCourseEntity.setCourseType(jsonObject.getInt("courseType"));
                studentCourseEntity.setCourseCredit(jsonObject.getInt("courseCredit"));
                studentCourseEntity.setTeacherName(jsonObject.getString("teacherName"));
                studentCourseEntity.setTeacherId(jsonObject.getString("teacherId"));
                studentCourse_adapter.add(studentCourseEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void doSaveSelectCourse(final int itemPosition) {
         LoadingX loadingX = new LoadingX(this);
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                    courseEntities.remove(itemPosition);
                    studentCourse_adapter.notifyDataSetChanged();
            }
        };
        baseRequest.doPost(HeaderUtil.getRequestParamsFromObject(courseEntities.get(itemPosition)),"student/saveselectcourse");
    }
}
