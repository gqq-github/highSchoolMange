package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.CourseRequest;
import cn.gq.highschoolmanger.entity.EditEntity;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.EidTextUtils;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {
    private MyAdapter<EditEntity> adapter ;
    private ArrayList<EditEntity> editEntities = new ArrayList<>() ;
    public static Context context ;
    private ListView listView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_mian);
        listView =  findViewById(R.id.lv_course_edit);
        findViewById(R.id.course_btn_add).setOnClickListener(this);
        init(listView);
    }
    public  Context getContext () {
        if(context == null) {
         context = getApplicationContext() ;
        }
        return context ;
    }
    public void init (ListView view) {

        adapter = new MyAdapter<EditEntity>(editEntities,R.layout.course_edit) {
            @Override
            public void bindView(ViewHolder holder, EditEntity obj) {
                holder.setText(R.id.tv_course_name,obj.name) ;
                holder.setEditText(R.id.et_course_edit,obj);
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        view.setAdapter(adapter);
        initdata();
    }
    public void  initdata () {

        editEntities.add( new EditEntity("课程编号  :","请输入课程编号","courseId")) ;
        editEntities.add( new EditEntity("课程名字  :","请输入课程名字","courseName")) ;
        editEntities.add( new EditEntity("课程学分  :","请输入课程学分","courseCredit")) ;
        editEntities.add( new EditEntity("课程介绍  :","请输入课程介绍","courseDetail")) ;
        editEntities.add( new EditEntity("课程日期  :","请输入课程日期","courseData")) ;
        adapter.add(editEntities);
    }
    public RequestParams getListViewData (ListView view) {
        RequestParams params = new RequestParams()  ;
        ArrayList<String> names  = new ArrayList<>();
        // todo : 使用的ListView进行数据的添加 ： 如果ListView
        // 能够进行滑动那么会存在异常，可以采用editEntitys中的数据进行
        // reposeValue中的数据
        for (int i = 0 ;i<editEntities.size() ; i++) {
            View childAt = view.getChildAt(i);
            params.put(editEntities.get(i).requestName,EidTextUtils.getStringVlue(childAt,R.id.et_course_edit));
           // HeaderUtil.setRequestParamByListView(params,editEntities.get(i).requestName,EidTextUtils.getStringVlue(view,R.id.et_course_edit) );
        }
        return  params ;
    }

    @Override
    public void onClick(View v) {
        RequestParams params = getListViewData(listView);
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据添加");
        loadingX.setMessage("数据添加中");
        new CourseRequest(loadingX) {
            @Override
            public void successDo() {
                for (int i =0 ;i <editEntities.size() ; i++) {
                    editEntities.get(i).setResponseValue("");
                    adapter.notifyDataSetChanged();
                }
            }
        }.addCourse("course/course_add",params);
    }
}
