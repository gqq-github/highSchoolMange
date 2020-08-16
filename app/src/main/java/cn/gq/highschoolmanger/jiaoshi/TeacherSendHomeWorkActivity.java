package cn.gq.highschoolmanger.jiaoshi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

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

public class TeacherSendHomeWorkActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner  spin_select_course;
    private EditText et_homeworker_question;
    private EditText et_homeworker_credit;
    private EditText et_homeworker_class;
    private EditText et_homeworker_time;
    private TextView homeworker_btn_add;
    private MyAdapter spin_select_course_adapter;
    private ArrayList<TeacherSelectCourseEntity> tCourses = new ArrayList<>();
    private Integer selectPosition ; // 记录选中的item
    LoadingX loadingX = new LoadingX(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_send_homeworke);
        init();
        initSpinner();
        doListener();
        initDate () ; // 初始化数据
    }
    public void initDate () {
        loadingX.setTitle("初始化");
        loadingX.setMessage("加载中");
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
                tCourses.add(teacherCourse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spin_select_course_adapter.notifyDataSetChanged();
    }

    public void init() {
        et_homeworker_question = findViewById(R.id.et_homeworker_question);
        et_homeworker_credit = findViewById(R.id.et_homeworker_credit);
        et_homeworker_class = findViewById(R.id.et_homeworker_class);
        et_homeworker_time = findViewById(R.id.et_homeworker_time);
        homeworker_btn_add = findViewById(R.id.homeworker_btn_add);
    }

    // 初始化Spinner组件
    public void initSpinner() {
        spin_select_course = findViewById(R.id.spin_select_course);
        spin_select_course_adapter = new MyAdapter<TeacherSelectCourseEntity>(tCourses, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, TeacherSelectCourseEntity obj) {
                //设置什么专业什么课程
                holder.setText(R.id.tv_spin,  "专业 :" +obj.getMajorName() + " 课程：" + obj.getCourseName());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_select_course.setAdapter(spin_select_course_adapter);
    }

    public void doListener() {
        homeworker_btn_add.setOnClickListener(this);
        spin_select_course.setOnItemSelectedListener(this);
    }
  //  最终的提交事件

    @Override
    public void onClick(View v) {
        TeacherSelectCourseEntity entity = tCourses.get(selectPosition);
        RequestParams params = new RequestParams();
        params.put("courseId",entity.getCourseId());
        params.put("courseName",entity.getCourseName());
        params.put("question",getEditTextValue(et_homeworker_question));
        params.put("courseTime",getEditTextValue(et_homeworker_time));
        params.put("className",getEditTextValue(et_homeworker_class));
        params.put("credit",getEditTextValue(et_homeworker_credit));
        params.put("majorId",entity.getMajorId());
        params.put("majorName",entity.getMajorName());
        loadingX.setTitle("作业发布");
        loadingX.setMessage("...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {

            }
        };
        baseRequest.doPost(params,"teacher/sendHomeWork");
    }
   public String getEditTextValue (EditText editText)  {
        return  editText.getText().toString();
   }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())  {
            case R.id.spin_select_course :
            selectPosition = position ;
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
