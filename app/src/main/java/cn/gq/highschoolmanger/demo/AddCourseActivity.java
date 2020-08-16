package cn.gq.highschoolmanger.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.CourseTable;
import cn.gq.highschoolmanger.entity.Major;
import cn.gq.highschoolmanger.loading.LoadingX;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String TAG ="BanJiActivity" ;
    private static Integer selectMajorPosition ;
    private  static Integer selectCoursePosition ;
    private Spinner spin_kecheng;
    private Spinner spin_zhuanye;
    private Context mContext;
    //判断是否为刚进去时触发onItemSelected的标志
    private boolean one_selected = false;
    private boolean two_selected = false;
    private MyAdapter spin_kecheng_adapter = null;
    private MyAdapter spin_zhuanye_adapter = null;
    private JSONArray responseData;
    private ArrayList<Course> coursesData = new ArrayList<>() ;
    private   ArrayList <Major>  majorsData = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_git);
        setFinishOnTouchOutside(false);
        mContext = AddCourseActivity.this ;
        bindZhuanyeSpinView();
        bindkechengSpinView();
        loadZhuanyeAndKeCheng();
        // 班级名称
        final EditText inputClassName = (EditText) findViewById(R.id.class_name);
        // 教师名称 ：动态设置
      //  final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        // 上课地点
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        // 上课在周几
        final EditText inputDay = (EditText) findViewById(R.id.week);
        // 在第几节课开始
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        // 在第几节课结束
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);

        Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = inputClassName.getText().toString();
               // String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
               // 进行数据的重组 ，将相关的信息保存到数据库 // 到时候加载课程表
                CourseTable courseTable = new CourseTable();
                // 1 . 获取当前的的课程信息
                Major major = majorsData.get(selectMajorPosition);
                courseTable.setMajorId(major.getMid());
                courseTable.setMajorName(major.getMname());
                Course course1 = coursesData.get(selectCoursePosition);
                courseTable.setCourseId(course1.courseId);
                courseTable.setCourseName(course1.getCourseName());
                courseTable.setCourseType(course1.courseType);
                courseTable.setTeacherName(course1.getTeacher());
                courseTable.setCmtId(course1.getCmtId());
                // 1

                if (className.equals("")|| classRoom.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {

                    //2 设置输入信息
                    courseTable.setClassName(className);
                    courseTable.setClassRoom(classRoom);
                    courseTable.setDay(Integer.valueOf(day));
                    courseTable.setStart(Integer.valueOf(start));
                    courseTable.setEnd(Integer.valueOf(end));
                    //2
                    Intent intent = new Intent(AddCourseActivity.this, kechengbiaoActivity.class);
                  //  intent.putExtra("course", course);
                    intent.putExtra("courseTable",courseTable);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void bindkechengSpinView() {
        spin_kecheng = findViewById(R.id.spin_kecheng);

        spin_kecheng_adapter = new MyAdapter<Course>(coursesData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, Course course) {
                holder.setText(R.id.tv_spin, course.getCourseName());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_kecheng.setAdapter(spin_kecheng_adapter);
        spin_kecheng.setOnItemSelectedListener(this);
    }

    public void bindZhuanyeSpinView()  {
        spin_zhuanye = findViewById(R.id.spin_zhuanye);
        spin_zhuanye_adapter = new MyAdapter<Major>(majorsData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, Major obj) {
                holder.setText(R.id.tv_spin, obj.getMname());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_zhuanye.setAdapter(spin_zhuanye_adapter);
        spin_zhuanye.setOnItemSelectedListener(this);
    }

    public void loadZhuanyeAndKeCheng() {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("专业课程加载中  >>>>>");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                // 更新学院数据
                try {
                    responseData = (JSONArray) data.get("data");
                    loadMajordata ();
                    // 请求成功之后进行初始更新spinner
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onSuccessDo: Json转化异常");
                }

            }
        } ;
        baseRequest.doGet(null,"opencourse/getcourseandmajor");
    }

    public void loadMajordata () throws JSONException {
              majorsData.clear();
        for (int i =0  ; i<responseData.length() ; i++) {
            JSONObject object = responseData.getJSONObject(i);
            Major major = new Major(object.getInt("majorId"), object.getString("majorName"));
            spin_zhuanye_adapter.add(major);
        }
        loadCourseData (0);
    }

    private void loadCourseData(int position) throws JSONException {
       JSONArray jsonArray =  responseData.getJSONObject(position).getJSONArray("courses") ;
         coursesData.clear();
       for (int i = 0 ; i<jsonArray.length() ; i++) {
           JSONObject jsonObject = jsonArray.getJSONObject(i);
           Course course = new Course();
           course.setCourseName(jsonObject.getString("courseName"));
           course.courseId = jsonObject.getString("courseId") ;
           course.courseType = jsonObject.getInt("courseType") ;
           course.setTeacher(jsonObject.getString("teacherName"));
           course.cmtId = jsonObject.getInt("cmtId") ;
           spin_kecheng_adapter.add(course);
       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId()) {
            case R.id.spin_kecheng:
                selectCoursePosition = position ;
                if (one_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);

                    Toast.makeText(mContext, "您选择的课程是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    one_selected = true;
                }
                break;
            case R.id.spin_zhuanye:
                selectMajorPosition = position ;
                if (two_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的转业是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    try {
                        loadCourseData(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    two_selected = true;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
