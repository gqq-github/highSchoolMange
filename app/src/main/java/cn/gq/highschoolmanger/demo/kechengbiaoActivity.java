package cn.gq.highschoolmanger.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.CourseTable;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HeaderUtil;

public class kechengbiaoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
   private List<View> views = new ArrayList<>() ;
    //星期几
    private RelativeLayout day;
    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_git);

        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }




    //创建"第几节数"视图
    private void createLeftView(CourseTable courseTable) {
        int endNumber = courseTable.getEnd();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.left_view_git, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final CourseTable courseTable) {
        int getDay = courseTable.getDay();
        if ((getDay < 1 || getDay > 7) || courseTable.getStart() > courseTable.getEnd())
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = findViewById(dayId);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card_git, null); //加载单个课程布局

            v.setY(height * (courseTable.getStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(courseTable.getEnd()-courseTable.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(courseTable.getClassName() +"\n"+courseTable.getCourseName() + "\n" + courseTable.getTeacherName() + "\n" + courseTable.getClassRoom()); //显示课程名
            views.add(v);
            day.addView(v);

            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setVisibility(View.GONE);//先隐藏
                    day.removeView(v);//再移除课程视图
//                    SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
//                    sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});
                    deleteData(courseTable);
                    return true;
                }
            });
        }
    }

    public void deleteData (CourseTable courseTable)  {
        BaseRequest baseRequest = new BaseRequest(new LoadingX(this)) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {

            }
        };
        baseRequest.setShow(false);
        RequestParams requestParamsFromObject = HeaderUtil.getRequestParamsFromObject(courseTable);
        baseRequest.doPost(requestParamsFromObject,"coursetable/deletecoursetable");
    }
  private  SearchView sv ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem item = menu.findItem(R.id.search);
        sv = (SearchView) item.getActionView();
        // 设置SearchView默认是否缩小为图标
        sv.setIconifiedByDefault(false);
        // 为组件设置监听
        sv.setOnQueryTextListener(this);
        // 设置显示搜索的按钮
        sv.setSubmitButtonEnabled(true);
        //设置默认显示的文本
        sv.setQueryHint("查找应用名");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_courses:
                Intent intent = new Intent(kechengbiaoActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 0);
                break;

            case  R.id.search :
                //todo :可以为搜索图标设置相应的事件处理
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
          //  Course course = (Course) data.getSerializableExtra("course");
            CourseTable courseTable = (CourseTable) data.getSerializableExtra("courseTable");
//            course.setCmtId(courseTable.getCmtId());
//            course.setClassName(courseTable.getClassName());
//            course.setCourseId(courseTable.getCourseId());
            //创建课程表左边视图(节数)
            createLeftView(courseTable);
            //创建课程表视图
            createItemCourseView(courseTable);
            //存储数据到数据库
           // saveData(course);
            saveData(courseTable);
        }
    }
    public void saveData (CourseTable courseTable) {
        RequestParams params = HeaderUtil.getRequestParamsFromObject(courseTable);
        LoadingX loadingX = new LoadingX(this);
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {

            }
        };
        baseRequest.setShow(false);
        baseRequest.doPost(params,"coursetable/addcoursetable");
    }
  public void delAllView () {
        if(views.size()==0) {
            return;
        }
        for (int i = 0 ; i<views.size() ; i++) {
            View view = views.get(i);
            view.setVisibility(View.GONE) ;
            day.removeView(view);
        }
        views.clear();
  }
    @Override
    public boolean onQueryTextSubmit(String query) {
        delAllView();
       if(query == null || query.equals("") ){
           Toast.makeText(this,"数据不能为空",Toast.LENGTH_SHORT).show();
       }else {
           loadCourseTableData(query);
       }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void loadCourseTableData (String query) {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setMessage("课程表加载中...");
        loadingX.setTitle("课程表");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                     doLoadData(data);
            }
        };
        baseRequest.doGet(null,"coursetable/coursetableshow/"+query);
    }
    public void doLoadData (JSONObject data) {
        try {
            JSONArray jsonArray = data.getJSONArray("data");
            List<CourseTable> res = new ArrayList<>() ;
             for (int i = 0 ; i<jsonArray.length() ; i++) {
                 res.add(loadView(jsonArray.getJSONObject(i)));
             }
            doLoadView(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // 将数据设置相应的视图进行显示
    public CourseTable loadView (JSONObject jsonObject) {
        CourseTable courseTable = new CourseTable();
        try {
            courseTable.setCourseId(jsonObject.getString("courseid"));
            courseTable.setStart(jsonObject.getInt("start"));
            courseTable.setEnd(jsonObject.getInt("end"));
           // courseTable.setDoubleWeek(jsonObject.);
           courseTable.setClassRoom(jsonObject.getString("classroom"));
           courseTable.setMajorId(jsonObject.getInt("majorid"));
           courseTable.setCourseName(jsonObject.getString("coursename"));
           courseTable.setMajorName(jsonObject.getString("majorname"));
           courseTable.setCmtId(jsonObject.getInt("cmtid"));
           courseTable.setTeacherName(jsonObject.getString("teachername"));
           courseTable.setCourseType(jsonObject.getInt("coursetype"));
           courseTable.setClassName(jsonObject.getString("classname"));
           courseTable.setDay(jsonObject.getInt("day"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  courseTable ;
    }

   // 使用数据进行渲染
    public void doLoadView (List<CourseTable> courseTables) {
           for (int i = 0 ; i<courseTables.size() ; i++) {
               createLeftView(courseTables.get(i));
               createItemCourseView(courseTables.get(i));
           }
    }
}