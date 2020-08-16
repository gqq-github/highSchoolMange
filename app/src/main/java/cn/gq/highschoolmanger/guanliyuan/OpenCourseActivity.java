package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.gq.highschoolmanger.entity.College;
import cn.gq.highschoolmanger.entity.Major;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.EidTextUtils;

public class OpenCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final String TAG ="BanJiActivity" ;
    private Integer collegeId ;
    private Integer majorId ;
    private  Integer course_type = 0;
    private Spinner spin_xueyuan;
    private Spinner spin_zhuanye;
    private Context mContext;
    //判断是否为刚进去时触发onItemSelected的标志
    private boolean one_selected = false;
    private boolean two_selected = false;
    private MyAdapter spin_xueyuan_adapter = null;
    private MyAdapter spin_zhuanye_adapter = null;
    private JSONArray responseData;
    private ArrayList <College> collegesData = new ArrayList<>() ;
    private   ArrayList <Major>  majorsData = new ArrayList<>() ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_opencourse_main);
        mContext = OpenCourseActivity.this ;
        try {
            bindXueYuanSpinView();
            bindZhuanyeSpinView();
            loadXueYuanAndBanjiData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        findViewById(R.id.open_course_btn_add).setOnClickListener(this);
        ((RadioGroup) findViewById(R.id.selectGroup)).setOnCheckedChangeListener(this);
    }
    public void loadXueYuanAndBanjiData() throws JSONException {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("学院专业加载中  >>>>>");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                // 更新学院数据
                try {
                    responseData = (JSONArray) data.get("data");

                    // 请求成功之后进行初始更新spinner
                    loadCollegesAndMajorData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onSuccessDo: Json转化异常");
                }

            }
        } ;
        baseRequest.doGet(null,"colleges/getcollegeandmajor");
    }
    public void loadCollegesAndMajorData () throws JSONException {
        for (int i = 0 ; i< responseData.length() ; i++) {
            College college = new College();
            JSONObject  temp = (JSONObject) responseData.get(i);
            college.setCid((Integer) temp.get("cid"));
            college.setCanme((String) temp.get("cname"));
            spin_xueyuan_adapter.add(college);
        }
        //加载学院数据
        //加载专业数据
        loadMajorData(0);

    }
    public void loadMajorData (int position) throws JSONException {
        JSONObject o = (JSONObject) responseData.get(position);
        JSONArray majors = (JSONArray) o.get("majors");
        majorsData.clear();
        for (int j = 0 ; j<majors.length(); j++) {
            Major major = new Major();
            JSONObject asJsonObject = (JSONObject) majors.get(j);
            major.setMid((Integer) asJsonObject.get("mid"));
            major.setMname((String) asJsonObject.get("mname"));
            spin_zhuanye_adapter.add(major);
        }

    }
    public void bindXueYuanSpinView() {
        spin_xueyuan = findViewById(R.id.spin_xueyuan);

        spin_xueyuan_adapter = new MyAdapter<College>(collegesData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, College college) {
                holder.setText(R.id.tv_spin, college.getCanme());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_xueyuan.setAdapter(spin_xueyuan_adapter);
        spin_xueyuan.setOnItemSelectedListener(this);
    }

    public void bindZhuanyeSpinView() throws JSONException {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_xueyuan:
                collegeId = collegesData.get(position).getCid();
                if (one_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);

                    Toast.makeText(mContext, "您选择的学院是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    try {
                        loadMajorData (position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                {
                    one_selected = true;
                }
                break;
            case R.id.spin_zhuanye:
                majorId = majorsData.get(position).getMid();
                if (two_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的转业是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                } else two_selected = true;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        RequestParams params = new RequestParams() ;
        params.put("collegeId",collegeId);
        params.put("majorId",majorId);
        String courseId = EidTextUtils.getStringVlue(this, R.id.et_opencourse_edit);
        params.put("courseId",courseId);
        params.put("courseType",course_type);
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("开设课程");
        loadingX.setMessage(".....");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
              EditText editText = findViewById(R.id.et_opencourse_edit);
              editText.setText(null);
            }
        };
        baseRequest.doPost(params,"opencourse/addopencourse");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.xuanxiu :
                course_type = 0 ;
                break;
            case  R.id.bixuan :
                course_type = 1 ;
                break;
            case  R.id.bixiu :
                course_type = 2 ;
                break;
        }
    }
}
