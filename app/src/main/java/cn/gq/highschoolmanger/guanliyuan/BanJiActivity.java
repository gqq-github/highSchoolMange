package cn.gq.highschoolmanger.guanliyuan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.College;
import cn.gq.highschoolmanger.entity.EditEntity;
import cn.gq.highschoolmanger.entity.Major;
import cn.gq.highschoolmanger.loading.LoadingX;

public class BanJiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private final String TAG ="BanJiActivity" ;
    private Integer collegeId ;
    private Integer majorId ;
    private Spinner spin_xueyuan;
    private Spinner spin_zhuanye;
    private Context mContext;
    //判断是否为刚进去时触发onItemSelected的标志
    private boolean one_selected = false;
    private boolean two_selected = false;
    private MyAdapter spin_xueyuan_adapter = null;
    private MyAdapter spin_zhuanye_adapter = null;
    private MyAdapter listView_banji_adapter = null;
   private   JSONArray responseData;
   private ArrayList <College> collegesData = new ArrayList<>() ;
   private   ArrayList <Major>  majorsData = new ArrayList<>() ;
    private ArrayList<EditEntity> editEntities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_banji_main);
        mContext = BanJiActivity.this ;
        try {
            bindXueYuanSpinView();
            bindZhuanyeSpinView();
            bindBanjiView();
          loadXueYuanAndBanjiData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
         findViewById(R.id.banji_btn_add).setOnClickListener(this);
    }
    public void loadXueYuanAndBanjiData() throws JSONException {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("数据初始化");
        loadingX.setMessage("学院专业加载中  >>>>>");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                   // 更新学院数据
                try {
                    responseData = (JSONArray) data.get("data");

                    // 请求成功之后进行初始更新spinner
                    loadCollegesAndMajorData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onSuccessDo: Json转化异常");
                }

            }
        } ;
        baseRequest.doGet(null,"colleges/getcollegeandmajor");
    }
     public void loadCollegesAndMajorData () throws JSONException {
        for (int i = 0 ; i< responseData.length() ; i++) {
            College college = new College();
              JSONObject  temp = (JSONObject) responseData.get(i);
            college.setCid((Integer) temp.get("cid"));
            college.setCanme((String) temp.get("cname"));
            spin_xueyuan_adapter.add(college);
        }
        //加载学院数据
        //加载专业数据
        loadMajorData(0);

     }
     public void loadMajorData (int position) throws JSONException {
         JSONObject o = (JSONObject) responseData.get(position);
         JSONArray majors = (JSONArray) o.get("majors");
         majorsData.clear();
         for (int j = 0 ; j<majors.length(); j++) {
             Major major = new Major();
             JSONObject asJsonObject = (JSONObject) majors.get(j);
             major.setMid((Integer) asJsonObject.get("mid"));
             major.setMname((String) asJsonObject.get("mname"));
             spin_zhuanye_adapter.add(major);
         }
         majorId = majorsData.get(0).getMid();
     }
    public void bindXueYuanSpinView() {
        spin_xueyuan = findViewById(R.id.spin_xueyuan);

        spin_xueyuan_adapter = new MyAdapter<College>(collegesData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, College college) {
                holder.setText(R.id.tv_spin, college.getCanme());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_xueyuan.setAdapter(spin_xueyuan_adapter);
        spin_xueyuan.setOnItemSelectedListener(this);
    }

    public void bindZhuanyeSpinView() throws JSONException {
        spin_zhuanye = findViewById(R.id.spin_zhuanye);
        spin_zhuanye_adapter = new MyAdapter<Major>(majorsData, R.layout.spin_item) {
            @Override
            public void bindView(ViewHolder holder, Major obj) {
                holder.setText(R.id.tv_spin, obj.getMname());
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        spin_zhuanye.setAdapter(spin_zhuanye_adapter);
        spin_zhuanye.setOnItemSelectedListener(this);
    }

    public void bindBanjiView() {
        ListView listView = findViewById(R.id.lv_banji_edit);
        listView_banji_adapter = new MyAdapter<EditEntity>(editEntities, R.layout.banji_item) {
            @Override
            public void bindView(final ViewHolder holder, EditEntity obj) {
                holder.setText(R.id.tv_banji_name, obj.name);
                holder.setEditText(R.id.et_banji_edit, obj);
                holder.setImageResource(R.id.iv_delete_icon, R.drawable.delete);
                holder.setOnClickListener(R.id.iv_delete_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editEntities.remove(holder.getItemPosition());
                        listView_banji_adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public String requestPath() {
                return null;
            }
        };
        listView.setAdapter(listView_banji_adapter);
        findViewById(R.id.iv_add_icon).setOnClickListener(this);
    }


    // 点击图片的时候出触发的事件
    @Override
    public void onClick(View v) {
        int tempid = v.getId();
        switch (tempid) {
            case R.id.iv_add_icon:
                editEntities.add(new EditEntity("班级名称", "请输入班级名称", "", ""));
                listView_banji_adapter.notifyDataSetChanged();
                break;
            case R.id.banji_btn_add:
                doSaveBanji();
                break;
        }
    }
    public void doSaveBanji () {
        RequestParams params = new RequestParams() ;
        params.put("cid",collegeId);
        params.put("mid",majorId);
        JSONArray jsonArray = new JSONArray() ;
        for (int i = 0 ; i<editEntities.size() ; i++) {
            jsonArray.put(editEntities.get(i).getResponseValue());
        }
        params.put("banjis",jsonArray);
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("班级添加");
        loadingX.setMessage("数据添加中>>>");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }

            @Override
            public void onSuccessDo(JSONObject data) {
                editEntities.clear();
                listView_banji_adapter.notifyDataSetChanged();
            }
        };
        baseRequest.doPost(params,"banji/addbanji");
    }

    // Spinner的Item点击的时候触发的事件
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_xueyuan:
                collegeId = collegesData.get(position).getCid();
                if (one_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的学院是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    try {
                        loadMajorData (position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                {
                    one_selected = true;
                }
                break;
            case R.id.spin_zhuanye:
                majorId = majorsData.get(position).getMid();
                if (two_selected) {
                    TextView txt_name = (TextView) view.findViewById(R.id.tv_spin);
                    Toast.makeText(mContext, "您选择的转业是~：" + txt_name.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                } else two_selected = true;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
