package cn.gq.highschoolmanger.jiaoshi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.TeacherSelectCourseEntity;
import cn.gq.highschoolmanger.entity.TeacherSignInEntity;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HeaderUtil;
import cn.gq.highschoolmanger.utils.SignIpUtils;
import cn.gq.highschoolmanger.utils.StringUtils;

public class CreateSignInActivity extends AppCompatActivity implements View.OnClickListener {
    private  TeacherSelectCourseEntity entity =null ;
  private TextView tv_btn_sign_end ;
  private TextView tv_btn_sign_start;
  private EditText et_sign_time ;
  private EditText et_class_name ;
  private WifiManager wifiManager;
  private DhcpInfo dhcpInfo;
  private Boolean isClick = false ;
  private  Integer findId = null ; // 用来查看签到情况的Id
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_signin);
        wifiManager = (WifiManager) getSystemService("wifi");
        dhcpInfo = wifiManager.getDhcpInfo() ;
        init();
    }
    public void init ()  {
        Intent intent = getIntent();
        entity = (TeacherSelectCourseEntity) intent.getSerializableExtra("teacherCourse");
        TextView tv_signin = findViewById(R.id.tv_signin_courseName);
        tv_signin.setText(entity.toString());
        et_class_name = findViewById(R.id.et_class_name);
        tv_btn_sign_end = findViewById(R.id.tv_btn_sign_end);
        tv_btn_sign_start = findViewById(R.id.tv_btn_sign_start);
        tv_btn_sign_start.setOnClickListener(this);
        tv_btn_sign_end.setOnClickListener(this);
        et_sign_time = findViewById(R.id.et_sign_time);

    }

    public void startTime (int timeAll)
    {
        CountDownTimer timer = new CountDownTimer(timeAll*1000*60,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
             tv_btn_sign_end.setText("("+millisUntilFinished/1000+"秒)");
            }

            @Override
            public void onFinish() {
                tv_btn_sign_end.setText("查  看");
                isClick=true;
                tv_btn_sign_end.setBackgroundResource(R.drawable.text_bg);
               // tv_btn_sign_start.setVisibility(View.VISIBLE);
              // et_sign_time.setFocusableInTouchMode(true);
            }
        };
        timer.start();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_btn_sign_start) {

           String value =  et_sign_time.getText().toString();
           String className = et_class_name.getText().toString();
            if(StringUtils.isEmpty(value)&&StringUtils.isEmpty(className)){
              return;
            }

            doCreateSignIn(Integer.valueOf(value),className);

        }else if (v.getId() == R.id.tv_btn_sign_end) {
             // 查看相关学生的到课情况
             if (!isClick){
                 return;
             }
              if(entity.getCourseType().equals("选修"))
              doSeeStudentStatus(entity.getCourseId(),0,findId);
              //表示非选修课
              else
              doSeeStudentStatus(entity.getCourseId(),1,findId);
        }
    }
     public void doCreateSignIn (final int keepTime,String className) {
         findId = null ;  // 清空Id
         LoadingX loadingX = new LoadingX(this);
         loadingX.setTitle("发布签到");
         loadingX.setMessage("...");
         BaseRequest baseRequest = new BaseRequest(loadingX) {
             @Override
             public void onFailureDo(int code) {

             }

             @Override
             public void onSuccessDo(JSONObject data) {
                 tv_btn_sign_start.setVisibility(View.GONE);

                 try {
                     findId = data.getInt("id");
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 startTime(keepTime);
                 et_sign_time.setFocusable(false) ;
             }
         };
         TeacherSignInEntity teacherSignInEntity = new TeacherSignInEntity(entity);
         teacherSignInEntity.setKeepTime(keepTime);
         teacherSignInEntity.setClassName(className);
         teacherSignInEntity.setVerifed(SignIpUtils.intToIp(dhcpInfo.ipAddress));
         baseRequest.doPost(HeaderUtil.getRequestParamsFromObject(teacherSignInEntity),"teacher/createsignin");
     }

     public void doSeeStudentStatus (String courseId,Integer courseType,Integer teacherId) {
          LoadingX loadingX = new LoadingX(this) ;
          loadingX.setTitle("数据加载");
          loadingX.setMessage("...");
          BaseRequest baseRequest = new BaseRequest(loadingX) {
              @Override
              public void onFailureDo(int code) {

              }
              @Override
              public void onSuccessDo(JSONObject data) {

                  try {
                      delDataToTeacherSeeStudentSignIdActivity( data.getJSONArray("data"));
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          } ;
          baseRequest.doGet(null,"teacher/"+courseId+"/"+courseType+"/"+findId);
     }
     public void delDataToTeacherSeeStudentSignIdActivity (JSONArray jsonArray) throws JSONException {
         HashSet<String> noSignSet = new HashSet<>();
         HashSet<String> isSignSet = new HashSet<>() ;
         // 因为后端对数据进行了排序 签到的信息在全部信息的前面
        for (int i = 0 ; i< jsonArray.length() ; i++) {
            JSONObject object = jsonArray.getJSONObject(i) ;
            String name = object.getString("name") ;
            String studentId = object.getString("studentId");
            Integer type = object.getInt("type") ;
            if ((type == 0)) {
                isSignSet.add(name + "--" + studentId);
            } else {
                if(!isSignSet.contains(name + "--" + studentId)){
                    noSignSet.add(name + "--" + studentId);
                }
            }
        }
         Intent intent = new Intent(CreateSignInActivity.this, TeacherSeeStudentSignIn.class);
         intent.putExtra("courseName", entity.getCourseName());
         intent.putExtra("className",et_class_name.getText().toString());
         Bundle bundle = new Bundle();
         bundle.putSerializable("isSign",isSignSet);
         bundle.putSerializable("noSign",noSignSet);
         intent.putExtra("sign",bundle);
         startActivity(intent);
         finish();
     }
}
