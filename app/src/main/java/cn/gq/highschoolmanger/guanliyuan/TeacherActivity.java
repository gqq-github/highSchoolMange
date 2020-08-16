package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import cn.gq.highschoolmanger.entity.EditEntity;
import cn.gq.highschoolmanger.loading.LoadingX;

public class TeacherActivity extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String TAG ="TeacherActivity" ;
    private Integer collegeId ;
    private Spinner spin_teacher;
    private boolean one_selected = false;
    private MyAdapter<College> collegeMyAdapter ;
    private MyAdapter<EditEntity> teacherInfoAdapte ;
    private ArrayList<EditEntity> editEntities = new ArrayList<>() ;
    private ArrayList<College> collegesData = new ArrayList<>() ;
    private ListView teacherListView ;
    private JSONArray responseData ;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teacher_main);
        mContext = TeacherActivity.this;
        initCollegeSpinner();
        initTeacherListView();
        findViewById(R.id.teacher_btn_add).setOnClickListener(this);
    }
    public void initCollegeSpinner () {
     spin_teacher = findViewById(R.id.spin_teacher);
      collegeMyAdapter = new MyAdapter<College>(collegesData, R.layout.spin_item) {
          @Override
          public void bindView(ViewHolder holder, College obj) {
              holder.setText(R.id.tv_spin, obj.getCanme());
          }

          @Override
          public String requestPath() {
              return null;
          }
      } ;
      spin_teacher.setAdapter(collegeMyAdapter);
      spin_teacher.setOnItemSelectedListener(this);
       loadCollegeData() ;
    }
    public void loadCollegeData(){

        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("学院加载中  >>>>");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                // 更新学院数据
                try {
                    responseData = (JSONArray) data.get("data");
                      setRquestDataToLocalData();
                    // 请求成功之后进行初始更新spinner
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "TeacherActivity : onSuccessDo: Json转化异常");
                }

            }
        } ;
        baseRequest.doGet(null,"colleges/getCollege");
    }
    public void setRquestDataToLocalData () throws JSONException {
        for (int i = 0 ; i< responseData.length() ; i++) {
              JSONObject jsonObject = (JSONObject) responseData.get(i);
              College college = new College();
              college.setCanme(jsonObject.getString("cname"));
              college.setCid(jsonObject.getInt("cid"));
              collegeMyAdapter.add(college);
        }
    }
    public void initTeacherListView () {
      teacherListView = findViewById(R.id.lv_teacher_edit);
      teacherInfoAdapte = new MyAdapter<EditEntity>(editEntities,R.layout.base_edit) {
          @Override
          public void bindView(ViewHolder holder, EditEntity obj) {
                 holder.setText(R.id.show_name,obj.name);
                 holder.setEditText(R.id.hint_name,obj);
          }

          @Override
          public String requestPath() {
              return null;
          }
      } ;
      teacherListView.setAdapter(teacherInfoAdapte);
      loadTeacherData();
    }
    public void loadTeacherData() {
        editEntities.add( new EditEntity("教师编号  :","教师编号","tid")) ;
        editEntities.add( new EditEntity("教师名字  :","请输入教师名字","tname")) ;
        editEntities.add( new EditEntity("教师学历  :","请输入教师学历","tdeg")) ;
        teacherInfoAdapte.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
           doSaveTeacherInfo();
    }
    public void doSaveTeacherInfo () {
        RequestParams params = new RequestParams();
        params.put("collegeId",collegeId);
        for (int i = 0 ; i<editEntities.size() ; i++) {
            EditEntity entity = editEntities.get(i);
            params.put(entity.getRequestName(),entity.responseValue);
        }
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("教师信息录入");
        loadingX.setMessage("教师信息录入中....");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {

            }
        };
        baseRequest.doPost(params,"teacher/addteacher");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_teacher:
                collegeId = collegesData.get(position).getCid();
                if (one_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的学院是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    one_selected = true;
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
