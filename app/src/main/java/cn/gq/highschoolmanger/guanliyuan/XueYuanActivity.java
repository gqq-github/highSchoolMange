package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.MainActivity;
import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.EditEntity;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.EidTextUtils;
import cn.gq.highschoolmanger.utils.LOCAL_USERINFO;

public class XueYuanActivity extends AppCompatActivity implements View.OnClickListener {
    private static  Context context ;
    private MyAdapter<EditEntity> adapter ;
    private ArrayList<EditEntity> editEntities = new ArrayList<>() ;
    private ListView listView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_xueyuan_main);
         listView = findViewById(R.id.lv_xueyuan_edit);
         bindListen();
         init();
    }
  public void initdata () {
        editEntities.add(new EditEntity("专业名称:","请输入专业名称","",""));
    }
    public void init () {
        adapter = new MyAdapter<EditEntity>(editEntities,R.layout.xueyuan_item) {
            @Override
            public void bindView(final ViewHolder holder,  EditEntity obj) {
                holder.setText(R.id.tv_zhuanye_name,obj.name) ;
                holder.setEditText(R.id.et_zhuanye_edit,obj);
                    holder.setImageResource(R.id.iv_delete_icon ,R.drawable.delete) ;
                    holder.setOnClickListener(R.id.iv_delete_icon, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editEntities.remove(holder.getItemPosition());
                            adapter.notifyDataSetChanged();
                        }
                    }) ;

            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        listView.setAdapter(adapter);
        initdata();
    }
 public void bindListen () {
        findViewById(R.id.xueyuan_btn_add).setOnClickListener(this);
        findViewById(R.id.iv_add_icon).setOnClickListener(this);
    }

    public Context getContext () {
        if(context == null) {
            context = getApplicationContext() ;
        }
        return context ;
    }

    @Override
    public void onClick(View v) {
       int  id = v.getId();
       switch (id)
       {
           case R.id.iv_add_icon :
               adapter.add(new EditEntity("专业名称:","请输入专业名称","cname"));
               break;
           case R.id.xueyuan_btn_add :
               doAdd();
               break;
       }
    }

    public RequestParams getListViewData () {
        RequestParams params = new RequestParams();
        JSONArray datas = new JSONArray() ;
        for (int i = 0; i < editEntities.size(); i++) {
            datas.put(editEntities.get(i).responseValue);
        }
         String cname = EidTextUtils.getStringVlue(findViewById(R.id.et_xueyuan_edit),R.id.et_xueyuan_edit) ;
         params.put("cname",cname);
         params.put("majors",datas);
        return  params ;
    }
    //将数据发送到服务端
    public void doAdd  () {
        RequestParams params = getListViewData();
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("院系添加");
        loadingX.setMessage("院系添加中");

        BaseRequest request = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {
                  if(code==401) {
                      LOCAL_USERINFO.delUserInfo(MainActivity.getContext());
                      //todo : 添加提示框
                      Intent intent = new Intent(context, MainActivity.class);
                      startActivity(intent);
                      finish();
                  }
            }

            @Override
            public void onSuccessDo(JSONObject data) {
                for (int i = 0 ; i<editEntities.size() ; i++) {
                    editEntities.get(i).setResponseValue("");
                    adapter.notifyDataSetChanged();
                }
                // 成功时候进行数据的清除操作
              EditText editText = findViewById(R.id.et_xueyuan_edit);
              editText.setText("");
            }
        };
        // 添加学院和专业
        request.doPost(params,"/colleges/addcoandmajor");
    }
}
