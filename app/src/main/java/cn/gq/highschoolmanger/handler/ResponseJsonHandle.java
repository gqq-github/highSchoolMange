package cn.gq.highschoolmanger.handler;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public abstract class ResponseJsonHandle extends BaseJsonHttpResponseHandler {
    private static String TAG = "JSON_handle" ;
    private JsonObject data ;
    private Context context ;
    public abstract void onFailure(int statusCode, String msg) ;
    protected abstract void onSuccess(JSONObject data) throws JSONException;

 public   ResponseJsonHandle() {}
 public  ResponseJsonHandle(Context context){
    this.context = context ;
 }
    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
        Log.i(TAG, "onSuccess: 请求数据成功");
         if(response==null) {
             Log.i(TAG, "onSuccess: 数据为空");
             return;
         }
        try {
            JSONObject jsonObject = new JSONObject(rawJsonResponse);
            int code = 0 ;
            if(!jsonObject.isNull("code")) {
                 code = jsonObject.getInt("code");
             }
            String msg = "联系管理员" ;
            if(statusCode!=200) {
                onFailure(code,msg);
            } else {

                onSuccess(jsonObject);
            }

        } catch (JSONException e) {
            Log.i(TAG, "json 解析失败 ");
        }
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
        Log.i(TAG, "onFailure: 请求数据失败");
        int statu = statusCode ;
        String msg="联系管理员" ;

        if(rawJsonData!=null&&rawJsonData.length()>0)
        try {
            JSONObject res = new JSONObject(rawJsonData);
            if(!res.isNull("code"))
            statu = res.getInt("code");

            if(!res.isNull("msg"))
            msg = res.getString("msg");
        } catch (JSONException e) {

        }

        System.out.println("失败"+rawJsonData);
        onFailure(statu,msg);
    }
    @Override
    protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {

        return isFailure ?null : new JSONObject(rawJsonData);
    }
}
