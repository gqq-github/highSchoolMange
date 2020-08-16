package cn.gq.highschoolmanger.doRequest;

import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.HttpClient;

public abstract class BaseRequest {
    private LoadingX loadingX ;
    private boolean isShow = true ;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public BaseRequest (LoadingX loadingX) {
        this.loadingX = loadingX ;
    }
    public BaseRequest (Boolean isShow) {
        this.isShow = isShow ;
    }
    public void doPost (RequestParams params,String path) {
        if(isShow) {
            loadingX.showProgressDialog();
        }
        HttpClient.doPost(path, params, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                Toast.makeText(loadingX.getContext(),msg,Toast.LENGTH_LONG).show(); ;
                if (isShow)
                loadingX.close();

            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                Toast.makeText(loadingX.getContext(),data.getString("msg"),Toast.LENGTH_LONG).show(); ;
                if (isShow)
                loadingX.close();
                onSuccessDo(data);
            }
        }) ;
    }
    public void doGet  (RequestParams params , String path) {
        if(isShow) {
            loadingX.showProgressDialog();
        }
        HttpClient.doGet(path, params, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                Toast.makeText(loadingX.getContext(),msg,Toast.LENGTH_LONG).show(); ;
                if (isShow)
                loadingX.close();
                onFailureDo(statusCode);
            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                Toast.makeText(loadingX.getContext(),data.getString("msg"),Toast.LENGTH_LONG).show(); ;
               if (isShow)
                loadingX.close();
              onSuccessDo(data);
            }
        }) ;

    }
    public abstract void onFailureDo (int code);
    public abstract void onSuccessDo (JSONObject data) ;

    // 实现 文件的上传

    public void doPost (String file,RequestParams params,String path) {
        if(isShow) {
            loadingX.showProgressDialog();
        }
        HttpClient.doPost(file,path, params, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                Toast.makeText(loadingX.getContext(),msg,Toast.LENGTH_LONG).show(); ;
                if (isShow)
                    loadingX.close();

            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                Toast.makeText(loadingX.getContext(),data.getString("msg"),Toast.LENGTH_LONG).show(); ;
                if (isShow)
                    loadingX.close();
                onSuccessDo(data);
            }
        }) ;
    }

}
