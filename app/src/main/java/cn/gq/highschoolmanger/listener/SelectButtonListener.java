package cn.gq.highschoolmanger.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gq.highschoolmanger.MainActivity;
import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.entity.userInfo;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.login.LoginApi;

/***
 *
 * @param <T>
 *  对注册问题的封装
 *
 */
public class SelectButtonListener<T extends AppCompatActivity> implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private T activity ;
    public static Context context = null;
    private userInfo userinfo ;
    public SelectButtonListener(T activity) {
        this.activity = activity ;
      context = activity.getApplication();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
         if (userinfo ==null) {
             userinfo = new userInfo(activity) ;
         }
        int student = this.activity.findViewById(R.id.student).getId();
        int teacher = this.activity.findViewById(R.id.teacher).getId();
      //  int manger = this.activity.findViewById(R.id.manger).getId();
         if(checkedId == student) {
             userinfo.setRole(2);
         }else if (checkedId == teacher) {
             userinfo.setRole(1);
         }else {
             userinfo.setRole(0);
         }
    }
    @Override
    public void onClick(View v) {
        doRegister();

            }
            private  void doRegister () {
                RequestParams requestParams = new RequestParams();
                requestParams.put("userId", userinfo.getUserId());
                requestParams.put("passWord", userinfo.getPassword());
                requestParams.put("role", userinfo.getRole());
                LoginApi.postRegister(requestParams, new ResponseJsonHandle() {
                    @Override
                    public void onFailure(int statusCode, String msg) {
                        Log.i("DO_LOGIN", "UI failure" + msg);
                        Toast.makeText( context, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(JSONObject data) throws JSONException {
                        Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });
            }
}
