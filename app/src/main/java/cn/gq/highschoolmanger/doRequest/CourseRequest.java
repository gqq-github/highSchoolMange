package cn.gq.highschoolmanger.doRequest;

import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HttpClient;
//todo : 该方法不在使用
public abstract class CourseRequest {
    private LoadingX loadingX ;
    public CourseRequest (LoadingX loadingX) {
        this.loadingX = loadingX ;
    }
    public void addCourse (String path, RequestParams params) {
        loadingX.showProgressDialog();
        HttpClient.doPost(path, params, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                Toast.makeText(loadingX.getContext(),"添加失败",Toast.LENGTH_LONG).show(); ;
                loadingX.close();
            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                Toast.makeText(loadingX.getContext(),"添加成功",Toast.LENGTH_LONG).show(); ;
                loadingX.close();
                successDo();

            }
        }) ;
    }
    public  abstract void  successDo ();
}
