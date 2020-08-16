package cn.gq.highschoolmanger.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.StudentSeeHomeworkEntity;
import cn.gq.highschoolmanger.loading.LoadingX;
import cz.msebera.android.httpclient.Header;

public class StudentSeeHomework extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spin_courseName ;
    private MyAdapter<String> spin_adapter;
    private ArrayList<String> spin_data = new ArrayList<>();
    private ListView lv_see_homework ;
    private MyAdapter<StudentSeeHomeworkEntity> lv_adapter ;
    private ArrayList<StudentSeeHomeworkEntity> lv_data = new ArrayList<>();
    private HashMap<String, ArrayList<StudentSeeHomeworkEntity>> vis = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_see_homework);
        init() ;
    }

    private void init() {
        spin_courseName = findViewById(R.id.spin_courseName);
        spin_adapter = new MyAdapter<String>(spin_data,R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, String obj) {
                 holder.setText(R.id.tv_spin,obj);
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_courseName.setAdapter(spin_adapter);
        spin_courseName.setOnItemSelectedListener(this);
        lv_see_homework = findViewById(R.id.lv_see_homework);
        lv_adapter = new MyAdapter<StudentSeeHomeworkEntity>(lv_data,R.layout.student_see_homework_item) {
            @Override
            public void bindView(final ViewHolder holder,  StudentSeeHomeworkEntity obj) {
                holder.setText(R.id.tv_homework_question,"问题："+obj.getQuestion());
                holder.setText(R.id.tv_homework_credit,"分数："+obj.getCredit());

                // -------
                AsyncHttpClient client= new AsyncHttpClient();
                client.get(obj.getTeacherPicture(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {
                            //创建工厂对象
                            BitmapFactory bitmapFactory = new BitmapFactory();
                            //工厂对象的decodeByteArray把字节转换成Bitmap对象
                            Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            //设置图片
                            holder.setImageResource(R.id.iv_teacher_picture,bitmap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        error.printStackTrace();
                    }
                });

                // -----------------

               // todo holder.setImageResource(R.id.iv_teacher_picture,obj.getTeacherPicture());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        lv_see_homework.setAdapter(lv_adapter);
       initData () ;
    }

    private void initData() {
        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    delJSONArrayToData (data.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // todo baseRequest.doGet(null,"student/findHomework");
        baseRequest.doGet(null,"student/findHomework_1");
    }

    private void delJSONArrayToData(JSONArray data) throws JSONException {

        for (int i = 0 ; i< data.length() ; i++) {
            JSONObject temp = data.getJSONObject(i);
            String courseName =  temp.getString("courseName");
            String question = temp.getString("question") ;
            String teacherPicture = temp.getString("teacherPicture");
            String credit = temp.getString("credit");
            StudentSeeHomeworkEntity entity = new StudentSeeHomeworkEntity(courseName,question,teacherPicture,credit);

            if (vis.containsKey(courseName)) {
                vis.get(courseName).add(entity);
            } else {
                ArrayList<StudentSeeHomeworkEntity> ssList = new ArrayList<>();
                ssList.add(entity);
                spin_data.add(courseName);
                vis.put(courseName,ssList);
            }
        }
       spin_adapter.notifyDataSetChanged();

    }

    public void setLv_see_homeworkData (int position) {
        lv_data.clear();
        lv_data.addAll(vis.get(spin_data.get(position)));
        lv_adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setLv_see_homeworkData(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
