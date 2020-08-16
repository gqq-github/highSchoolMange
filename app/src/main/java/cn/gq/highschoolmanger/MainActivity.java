package cn.gq.highschoolmanger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gq.highschoolmanger.entity.userInfo;
import cn.gq.highschoolmanger.guanliyuan.ManngerActivity;
import cn.gq.highschoolmanger.jiaoshi.TeacherMainActivity;
import cn.gq.highschoolmanger.listener.ButtonClickListener;
import cn.gq.highschoolmanger.student.StudentMainActivity;
import cn.gq.highschoolmanger.utils.HeaderUtil;
import cn.gq.highschoolmanger.utils.LOCAL_USERINFO;

/**
 * 主ConText
 */
public class MainActivity extends AppCompatActivity {
   private static String TAG = "MAINACTIVITY" ;
   public static final String TAG_EXIT = "exit";
  public static Context CONTEXT = null ;
  private String userName ;
  private  static String role  ;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getApplicationContext();
        // todo: 验证打开页面的时候用户是否已经登录过了
         nextIntent();
         setContentView(R.layout.login);
        View signUp = findViewById(R.id.to_sign);
          // 到达注册页面的途径
          signUp.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                  startActivity(intent);
                  finish();
              }
          });
        userInfo user = new userInfo<>();
        ((RadioGroup)findViewById(R.id.selectGroup)).setOnCheckedChangeListener(new ButtonClickListener<MainActivity, userInfo>(R.id.selectGroup,user,this));
        // 为登录按钮来绑定事件
        // 需要进行后端传送的数据
        List<String> lis = new ArrayList<>() ;
        lis.add("userId");
        lis.add("passWord") ;
        lis.add("role") ;
        findViewById(R.id.main_btn_login).setOnClickListener(new ButtonClickListener<MainActivity, userInfo>(R.id.main_btn_login,user,this).setFiledNames(lis)) ;
    }
    public static Context getContext ()  {
        return CONTEXT ;
    }
    private Boolean isDoNextIntent ()
    {

        Map<String, String> userInfo = LOCAL_USERINFO.getUserInfo(this);
        if(userInfo!=null&&userInfo.size()!=0) {
            if(! (role = userInfo.get("role")).equals("role")) {

                return true ;
            }

        }
        return false ;
    }
    private void nextIntent () {
        if(isDoNextIntent()) {
            HeaderUtil.setHeader(this) ;
            //Intent intent = new Intent(kechengbiaoActivity.this, HomePageActivity.class);
           doNext();
        }

    }
 public void  doNext () {
        Intent intent ;
        if(role.equals("0")) {
            intent = new Intent(MainActivity.this, ManngerActivity.class);
        }else if (role.equals("1")){
            intent = new Intent(MainActivity.this, TeacherMainActivity.class);

        }else {
            intent = new Intent(MainActivity.this , StudentMainActivity.class) ;
        }
        startActivity(intent);
        finish();
 }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
