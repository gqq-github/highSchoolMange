package cn.gq.highschoolmanger.listener;

import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.guanliyuan.ManngerActivity;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.jiaoshi.TeacherMainActivity;
import cn.gq.highschoolmanger.login.LoginApi;
import cn.gq.highschoolmanger.student.StudentMainActivity;
import cn.gq.highschoolmanger.utils.CheckButtonUtils;
import cn.gq.highschoolmanger.utils.EidTextUtils;
import cn.gq.highschoolmanger.utils.HeaderUtil;
import cn.gq.highschoolmanger.utils.HttpClient;
import cn.gq.highschoolmanger.utils.LOCAL_USERINFO;
import cn.gq.highschoolmanger.utils.ReflectUtil;

/***
 * @param <T>
 *          v 泛型的参数是相关的JavaBean
 */
public class ButtonClickListener<T extends AppCompatActivity, V> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "ButtonClickListener";
    private int id;
    private V v;
    private T t;
    private List<String> filedNames;

    public ButtonClickListener(int id, V v, T t) {
        this.id = id;
        this.v = v;
        this.t = t;
    }

    @Override
    public void onClick(View v) {
        switch (id) {
            case R.id.main_btn_login:
                doLogin();
                break;

        }
    }

    public ButtonClickListener<T,V> setFiledNames(List<String> filedNames) {
        this.filedNames = filedNames;
        return this ;
    }

    private void doLogin() {
        String userId = EidTextUtils.getStringVlue(t, R.id.userId);
        String passWord = EidTextUtils.getStringVlue(t,R.id.password);
        ReflectUtil.setFieldValue(v,"userId",userId);
        ReflectUtil.setFieldValue(v,"passWord",passWord);
        RequestParams requestParamsByFileName = HeaderUtil.getRequestParamsByFileName(v, filedNames);
        LoginApi.postLogin(requestParamsByFileName, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                Toast.makeText(t,msg, Toast.LENGTH_LONG).show();
            }
            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                System.out.println(data);
                // todo 将收到的数据保存到本地
                String userId = data.getString("userId") ;
                String role = data.getInt("role")+"" ;
                String Authorization = "qxx:"+data.getString("Authorization") ;
                LOCAL_USERINFO.saveUSER(t,userId,role,Authorization);
                HttpClient.setHeaders(LOCAL_USERINFO.getUserInfo(t));
                Intent intent = null;
                if(role.equals("0")) {
                   intent = new Intent(t, ManngerActivity.class);
                }else if (role.equals("2")) {
                    intent = new Intent(t, StudentMainActivity.class) ;
                } else  {
                    intent = new Intent(t, TeacherMainActivity.class) ;
                }

                t.startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (id) {
            case R.id.selectGroup :
                setRoleValue();
                break;
        }

    }
    public void setRoleValue () {
        int role = CheckButtonUtils.getRole(id);
        ReflectUtil.setFieldValue(v,"role",role);
    }
}
