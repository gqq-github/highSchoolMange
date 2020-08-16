package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.MainActivity;
import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.config.MessageType;
import cn.gq.highschoolmanger.demo.kechengbiaoActivity;
import cn.gq.highschoolmanger.entity.Content_Icon;
import cn.gq.highschoolmanger.entity.MessageEntity;
import cn.gq.highschoolmanger.handler.IMyDo;
import cn.gq.highschoolmanger.handler.MY_SYNHandler;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.utils.BannerX;
import cn.gq.highschoolmanger.utils.DelResponse;
import cn.gq.highschoolmanger.utils.LOCAL_USERINFO;

public class ManngerActivity extends AppCompatActivity {
    private MyAdapter<Content_Icon> iconMyAdapter ;
    private ArrayList<Content_Icon> icon_data = new ArrayList<>() ;
    public static Context context  ;
    MY_SYNHandler  my_synHandler = new MY_SYNHandler() ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext() ;
        setContentView(R.layout.mannger_mian);
        new BannerX((Banner) this.findViewById(R.id.banner));
        initIcon();
        doExit();
    }




     public void initIcon() {
       GridView gridView =  findViewById(R.id.gv_icon);
       iconMyAdapter = new MyAdapter<Content_Icon>(icon_data,R.layout.iocn_item) {
           @Override
           public void bindView(ViewHolder holder, Content_Icon obj) {
               holder.setText(R.id.tv_icon,obj.iconName);
               holder.setImageResource(R.id.iv_icon,obj.base64) ;
           }
           @Override
           public String requestPath() {
               return "main/contentIcon";
           }
       } ;
       iconMyAdapter.SyNNotifyDataSetChanged(my_synHandler, new IMyDo<Content_Icon>() {
           @Override
           public void bindMessageAndAdapter(Message msg, MyAdapter<Content_Icon> adapter) {
               MessageEntity messageEntity = (MessageEntity) msg.obj;
               if (messageEntity.message_what==1 && messageEntity.message_type.equals(MessageType.ICON_MESSAGE)) {
                   adapter.notifyDataSetChanged();
               }
           }
       });
       gridView.setAdapter(iconMyAdapter);
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Toast.makeText(getApplication(), "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
               goOtherIntent(position);
           }
       });
       loadData();

    }
  public void  goOtherIntent (int pos) {
      switch (pos) {
          case 0 :
              //院系管理
              nextIntent(XueYuanActivity.class);
              break;
          case 1 :
              // 教务管理
              nextIntent(TeacherActivity.class);
              break;

          case 2 :
              // 班级管理
              nextIntent(BanJiActivity.class);
              break;
          case 3 :
              //添加课程
              nextIntent(CourseActivity.class);
              break;

          case 4 :
              // 开设课程
              nextIntent(OpenCourseActivity.class);
              break;
          case 5 :
              // 课表管理
              nextIntent(kechengbiaoActivity.class);
              break;

      }
  }
  public void nextIntent (Class classz) {
       Intent intent = new Intent(this,classz) ;
       startActivity(intent);

  }
    public void loadData () {

       iconMyAdapter.doGetRequest(null, new ResponseJsonHandle() {
           @Override
           public void onFailure(int statusCode, String msg) {
               if(statusCode==401) {
                   LOCAL_USERINFO.delUserInfo(MainActivity.getContext());
                   //todo : 添加提示框
                   Intent intent = new Intent(context, MainActivity.class);
                   startActivity(intent);
               }
               else  {
                   Toast.makeText(context,"msg",Toast.LENGTH_LONG) ;
               }
           }

           @Override
           protected void onSuccess(JSONObject data) throws JSONException {
               DelResponse.delIconJsonObject(icon_data,data,my_synHandler);
           }
       });
    }
    public void doExit ()
    {
        findViewById(R.id.main_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //todo : 添加提示框
                //
                LOCAL_USERINFO.delUserInfo(MainActivity.getContext());
              System.exit(0);
            }
        });
    }
}

