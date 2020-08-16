package cn.gq.highschoolmanger.student;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
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
import cn.gq.highschoolmanger.entity.ClassEntity;
import cn.gq.highschoolmanger.entity.College;
import cn.gq.highschoolmanger.entity.Major;
import cn.gq.highschoolmanger.entity.Student;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HeaderUtil;

public class StudentInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Integer collegeId ;
    private Integer majorId ;
    private Integer classId ;
    private Integer collegePosition = 0;
    private Integer majorPosition = 0;
    private Integer classPosition = 0;
    private Spinner college_spinner;
    private Spinner major_spinner;
    private Spinner class_spinner;
    private boolean one_selected = false;
    private boolean two_selected = false;
    private boolean three_selected = false;
    private MyAdapter college_adapter;
    private MyAdapter major_adapter;
    private MyAdapter class_adapter;
    private JSONArray responseData;
    private ArrayList<College> collegesData = new ArrayList<>();
    private ArrayList<Major> majorsData = new ArrayList<>();
    private ArrayList<ClassEntity> classData = new ArrayList<>();
    private Context mContext ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_mian_info);
        mContext = StudentInfoActivity.this ;
        init();
        findViewById(R.id.tijiao_btn_add).setOnClickListener(this);
    }

    public void init() {
        innitCollegeSpinner();
        innitMajorSpinner();
        innitClassSpinner();
        loadData();
    }

    public void innitCollegeSpinner() {
        college_spinner = findViewById(R.id.spin_xueyuan);
        college_adapter = new MyAdapter<College>(collegesData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, College obj) {
                holder.setText(R.id.tv_spin, obj.getCanme());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        college_spinner.setAdapter(college_adapter);
        college_spinner.setOnItemSelectedListener(this);

    }

    public void innitMajorSpinner() {
        major_spinner = findViewById(R.id.spin_zhuanye);
        major_adapter = new MyAdapter<Major>(majorsData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, Major obj) {
                holder.setText(R.id.tv_spin, obj.getMname());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        major_spinner.setAdapter(major_adapter);
        major_spinner.setOnItemSelectedListener(this);

    }

    public void innitClassSpinner() {
        class_spinner = findViewById(R.id.spin_banji);
        class_adapter = new MyAdapter<ClassEntity>(classData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, ClassEntity obj) {
                holder.setText(R.id.tv_spin, obj.getClassName());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        class_spinner.setAdapter(class_adapter);
        class_spinner.setOnItemSelectedListener(this);

    }


    public void loadData() {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("初始化");
        loadingX.setMessage("加载中");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    System.out.println(data.toString());
                    responseData = data.getJSONArray("data");
                    doInitData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        baseRequest.doGet(null, "colleges/getcollegeandmjorandclass");
    }

    public void doInitData() throws JSONException {
        for (int i = 0; i < responseData.length(); i++) {
            initCollegeData(i);
        }
        collegeId = collegesData.get(0).getCid() ;
        initMajorData(0);
        initClassData(0, 0);

    }

    public void initCollegeData(int position) throws JSONException {
        JSONObject object = responseData.getJSONObject(position);
        College college = new College();
        college.setCid(object.getInt("collegeId"));
        college.setCanme(object.getString("collegeName"));
        college_adapter.add(college);

    }

    public void initMajorData(int collegePosition) throws JSONException {
        JSONArray jsonArray = responseData.getJSONObject(collegePosition).getJSONArray("majors");
        majorsData.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Major major = new Major();
            major.setMid(jsonObject.getInt("majorId"));
            major.setMname(jsonObject.getString("majorName"));
            major_adapter.add(major);
        }
        majorId = majorsData.get(0).getMid() ;
    }

    public void initClassData(int collegePosition, int majorPosition) throws JSONException {
        JSONObject jsonObject = responseData.getJSONObject(collegePosition);
        JSONObject majors_object = jsonObject.getJSONArray("majors").getJSONObject(majorPosition);
        JSONArray classs = majors_object.getJSONArray("classs");
        classData.clear();
        for (int i = 0; i < classs.length(); i++) {
            JSONObject classObject = classs.getJSONObject(i);
            ClassEntity classEntity = new ClassEntity();
            classEntity.setClassId(classObject.getInt("classId"));
            classEntity.setClassName(classObject.getString("className"));
            class_adapter.add(classEntity);
        }
        classId = classData.get(0).getClassId() ;
    }

    @Override
    public void onClick(View v) {
      //  this.finish();
        LoadingX loadingX = new LoadingX(this);
        loadingX.setMessage("加载...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                   finish();
            }
        };
        Student student = new Student(collegeId, majorId, classId);
        baseRequest.doPost(HeaderUtil.getRequestParamsFromObject(student),"student/upinfo");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_xueyuan:
                collegeId = collegesData.get(position).getCid();
                collegePosition = position ;
                if (one_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的学院是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    try {
                       initMajorData(position);
                       initClassData(position,0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    one_selected = true;
                }
                break;
            case R.id.spin_zhuanye:
                majorPosition = position ;
                majorId = majorsData.get(position).getMid();
                if (two_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的转业是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    try {
                        initClassData(collegePosition,position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else two_selected = true;
                break;
            case  R.id.spin_banji :
                classId = classData.get(position).getClassId() ;
                if(three_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的班级是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                } else  {
                    three_selected = true ;
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
