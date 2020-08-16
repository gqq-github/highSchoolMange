package cn.gq.highschoolmanger.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.StudentCourseEntity;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HeaderUtil;

public class StudentQianDaoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "StudentQianDaoActivity";
    private WifiManager wifiManager;
    private DhcpInfo dhcpInfo;
    private StudentCourseEntity studentCourse;
    private TextView tv_student_signin_courseName;
    private TextView tv_btn_sign_student;

    private JSONObject validate ;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_signin);
        wifiManager = (WifiManager) getSystemService("wifi");
        dhcpInfo = wifiManager.getDhcpInfo();
        Intent intent = getIntent();
        studentCourse = (StudentCourseEntity) intent.getSerializableExtra("studentCourse");
        init();
    }

    public void init() {
        tv_btn_sign_student = findViewById(R.id.tv_btn_sign_student);
        tv_btn_sign_student.setOnClickListener(this);
        tv_student_signin_courseName = findViewById(R.id.tv_student_signin_courseName);
        tv_student_signin_courseName.setText(studentCourse.toString());

        initBtn();
    }

    public void initBtn() {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据加载");
        loadingX.setMessage("...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                 delData(data);
            }
        };
        baseRequest.doPost(HeaderUtil.getRequestParamsFromObject(studentCourse), "student/signin");
    }

    public void delData(JSONObject data) {
        String btShow = "";
        try {
            JSONObject data1 = data.getJSONObject("data");
            switch (data1.getInt("type")) {
                case 0:
                    btShow = "没有签到";
                    break;
                case 1:
                    btShow = "签  到";
                    tv_btn_sign_student.setClickable(true);
                    tv_btn_sign_student.setBackgroundResource(R.drawable.text_bg);
                    validate = data1.getJSONObject("validate");
                    break;
                case 2 :
                    btShow = "已 签 到";
                    break;
                case 3 :
                    btShow = "未 签 到";
                    break;
                case 4 :
                    btShow = "已 签 到";

            }
            tv_btn_sign_student.setText(btShow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        try {
              String verifed = validate.getString("verifed");
              Long endTime  = validate.getLong("endTime");
              Integer teacher_signinid = validate.getInt("signinid");
              String myVerifed = intToIp(dhcpInfo.ipAddress);
              Long currentTime = System.currentTimeMillis();
              if(!isSameIp(verifed,myVerifed)) {
                  // 不在签到区域
                  Toast.makeText(this,"不在签到区域", Toast.LENGTH_SHORT).show();
              }else  {

                  if (currentTime<endTime) {
                      doSignIn(teacher_signinid,currentTime);
                  }else {
                      // 签到已超时
                      tv_btn_sign_student.setClickable(false);
                      tv_btn_sign_student.setBackgroundColor(R.color.bt_textLog);
                      tv_btn_sign_student.setText("未 签 到");
                  }

              }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public boolean isSameIp (String verifed , String myverfed)  {
        verifed = verifed.substring(0,verifed.lastIndexOf(".")) ;
        myverfed = myverfed.substring(0,myverfed.lastIndexOf("."));
        if(verifed.equals(myverfed)) {
            return  true ;
        }
       return  false ;
    }
    public void doSignIn(Integer teacher_signinid, Long currentTime) {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("签到");
        loadingX.setMessage("签到中...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccessDo(JSONObject data) {
                tv_btn_sign_student.setClickable(false);
                tv_btn_sign_student.setBackgroundColor(R.color.bt_textLog);
                tv_btn_sign_student.setText("已签到");
            }
        };
        RequestParams params = new RequestParams();
        params.put("signInId",teacher_signinid);
        params.put("signInTime",currentTime);
        baseRequest.doPost(params,"student/creatsignin");
    }
}
