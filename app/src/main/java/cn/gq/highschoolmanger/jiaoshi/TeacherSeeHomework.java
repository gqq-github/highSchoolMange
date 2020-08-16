package cn.gq.highschoolmanger.jiaoshi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.ClassEntity;
import cn.gq.highschoolmanger.entity.StudentHomeworkTeacher;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.BitMapUriFileUtils;
import cz.msebera.android.httpclient.Header;
import me.kareluo.imaging.IMGEditActivity;

public class TeacherSeeHomework extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LoadingX loadingX = new LoadingX(this);
    private Spinner spin_class;
    private ListView lv_show_student_homework;
    private MyAdapter<ClassEntity> spin_class_adapter;
    private MyAdapter<StudentHomeworkTeacher> lv_show_student_homework_adapter;
    private ArrayList<ClassEntity> classs = new ArrayList<>();
    private ArrayList<StudentHomeworkTeacher> shts = new ArrayList<>();
    private ImageView iv_answer;
    private Bitmap bitmap;
   private  Integer holderPosition ;
   private  Integer spinnerPosition ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_see_homework);
        //  iv_homework_student = findViewById(R.id.iv_homework_student) ;
        init();
        loadData();
    }

    public void init() {
        initSpinner();
        initListView();
    }

    private void initListView() {
        lv_show_student_homework = findViewById(R.id.lv_show_student_homework);
        lv_show_student_homework_adapter = new MyAdapter<StudentHomeworkTeacher>(shts, R.layout.teacher_see_homework_item) {
            @Override
            public void bindView(final ViewHolder holder, StudentHomeworkTeacher obj) {
                holder.setText(R.id.tv_homework_info, showText(obj));
                if(obj.getBitmap()!=null) {
                    holder.setImageResource(R.id.iv_answer,obj.getBitmap());
                } else {
           //   todo   holder.setImageResource(R.id.iv_answer, obj.getAnswerImage());
                     AsyncHttpClient client= new AsyncHttpClient();
                    client.get(obj.getAnswerImage(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                //创建工厂对象
                                BitmapFactory bitmapFactory = new BitmapFactory();
                                //工厂对象的decodeByteArray把字节转换成Bitmap对象
                                Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                                //设置图片
                                holder.setImageResource(R.id.iv_answer,bitmap);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                        }
                    });


          //




                }
                holder.setText(R.id.tv_homework_credit,obj.getEditEntity().name);
                holder.setEditText(R.id.et_homeworker_credit,obj.getEditEntity());
                holder.setOnClickListener(R.id.bt_pigai, new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        button.setClickable(false);
                        button.setBackgroundColor(R.color.bt_textLog);
                      doPigaiToServer(shts.get(holder.getItemPosition()));
                    }
                }) ;
                holder.setOnClickListener(R.id.iv_answer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holderPosition = holder.getItemPosition();
                        iv_answer = (ImageView) v;
                        bitmap = ((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap();
                        File test = BitMapUriFileUtils.saveBitmapFile(bitmap, "test");
                        Intent intent = new Intent(TeacherSeeHomework.this, IMGEditActivity.class);
                        Uri uri = Uri.fromFile(test);
                        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, uri);
                        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, "test");
                        startActivityForResult(intent, 0x1111);
                    }
                });
            }

            @Override
            public String requestPath() {
                return null;
            }

            public String showText(StudentHomeworkTeacher obj) {
                String res =
                        "课程名字：" + obj.getCourseName() + "\n" +
                                "学号：" + obj.getStudentId() + "\n" +
                                "姓名：" + obj.getStudentName() +"\n"+
                                "问题：" + obj.getQuestion();

                return res;
            }
        };
        lv_show_student_homework.setAdapter(lv_show_student_homework_adapter);
    }

    private void initSpinner() {
        spin_class = findViewById(R.id.spin_class);
        spin_class_adapter = new MyAdapter<ClassEntity>(classs, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, ClassEntity obj) {
                holder.setText(R.id.tv_spin, obj.getClassName());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_class.setAdapter(spin_class_adapter);
        spin_class.setOnItemSelectedListener(this);
    }


    public void loadData() {
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("...");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    initData(data.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        baseRequest.doGet(null, "teacher/getStudentsHomework_1");

        // todo baseRequest.doGet(null, "teacher/getStudentsHomework");
    }
    private HashMap<String,ArrayList<StudentHomeworkTeacher>> vis = new HashMap<>();
    public void initData(JSONArray jsonArray) {
        try {
           for (int i = 0 ; i<jsonArray.length() ; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                StudentHomeworkTeacher studentHomeworkTeacher = new StudentHomeworkTeacher();
                studentHomeworkTeacher.setAnswerId(obj.getInt("id"));
                studentHomeworkTeacher.setAnswerImage(obj.getString("image"));
                studentHomeworkTeacher.setQuestion(obj.getString("question"));
                studentHomeworkTeacher.setClassName(obj.getString("className"));
                studentHomeworkTeacher.setStudentId(obj.getString("studentId"));
                studentHomeworkTeacher.setStudentName(obj.getString("studentName"));
                studentHomeworkTeacher.setCourseName(obj.getString("courseName"));
               if(vis.containsKey(studentHomeworkTeacher.getClassName())) {
                   vis.get(studentHomeworkTeacher.getClassName()).add(studentHomeworkTeacher);
               } else {
                  classs.add(new ClassEntity(studentHomeworkTeacher.getClassName()));
                  ArrayList<StudentHomeworkTeacher> temp  =  new ArrayList<>();
                  temp.add(studentHomeworkTeacher);
                  vis.put(studentHomeworkTeacher.getClassName(),temp);
               }
           }
           spin_class_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
     //   initListViewData(0);
    }
    public void initListViewData (int postion) {
       ArrayList<StudentHomeworkTeacher> temp = vis.get(classs.get(postion).getClassName());
        shts.clear();
        shts.addAll(temp);
        lv_show_student_homework_adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_class:
                spinnerPosition = position ;
                initListViewData(spinnerPosition);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x1111 :
                if(resultCode==RESULT_OK) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(),
                            "test" + ".jpg");
                    Uri fromFile = Uri.fromFile(tempFile);
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(fromFile));
                        shts.get(holderPosition).setBitmap(bitmap);
                        iv_answer.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }



            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    public void doPigaiToServer (StudentHomeworkTeacher piGai) {
        Bitmap bitmap = piGai.getBitmap();
        Integer answerId = piGai.getAnswerId();
        String responseValue = piGai.getEditEntity().getResponseValue();
//  todo     ByteArrayOutputStream pictureOut = new ByteArrayOutputStream();
//  todo     bitmap.compress(Bitmap.CompressFormat.PNG,80,pictureOut);
//  todo     byte[] bytes = pictureOut.toByteArray();
//  todo     try {
//  todo         pictureOut.close();
//  todo     } catch (IOException e) {
//  todo         e.printStackTrace();
//  todo     }
//  todo     String teacherPic = Base64.encodeToString(bytes,0,bytes.length,Base64.DEFAULT);

        loadingX.setTitle("作业批改");
        loadingX.setMessage("批改...");
        RequestParams params = new RequestParams();
        params.put("answerId",answerId);
        params.put("credit",responseValue);
        try { // todo 去掉Try Catch
            params.put("check_homework_file",BitMapUriFileUtils.saveBitmapFile(bitmap,"temp"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {

            }
        };
        //批改作业
//      todo   baseRequest.doPost(params,"teacher/check");
        baseRequest.doPost(null,params,"teacher/check_1");
    }
}
