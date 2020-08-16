package cn.gq.highschoolmanger.handler;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;


public abstract class ResponseHandler<T> extends TextHttpResponseHandler {
    private static final String TAG = "ResponseHandler";
    private static final int STATUS_OK = 200;
    private Type resultType;
    public ResponseHandler() {
        this(null);
    }

    public ResponseHandler( Type resultType) {
        super();
        this.resultType = resultType;
    }

    public abstract void onFailure(int statusCode, int errorCode, String msg);
    public abstract void onSuccess(T result);
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(TAG, "<< 网络请求失败：" + responseString);
        String msg = null;
        int code=-1;
        if (statusCode != 0 && !TextUtils.isEmpty(responseString)) {
            try {
                System.out.println(responseString);
              //  JSONObject jsonObject = new JSONObject(responseString);
//                code = jsonObject.getInt("code");
//                msg = jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "网络请求失败";
        }
        onFailure(statusCode, code, msg);

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if (statusCode!=STATUS_OK){
            onFailure(statusCode, headers, responseString, null);
            return;
        }

        Log.d(TAG, "<< 网络请求成功：" + responseString);
        if (TextUtils.isEmpty(responseString)) {
            onFailure(statusCode, -1, "返回数据异常");
            return;
        }

     Type type = getResultType();
//        System.out.println(type);
          //获得子类的泛型类型
        String className = TypeToken.get(type).getRawType().getName();
        if (className.equals(String.class.getName())) {
            onSuccess((T) responseString);
            return;
        }
        T result;
        try {

            result = new Gson().fromJson(responseString, type);
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(statusCode,-1, "返回数据解析失败");
            return;
        }
        onSuccess(result);
    }

    private Type getResultType() {
        if (resultType == null) {
            Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
            resultType = types[0];
        }
        return resultType;
    }
}

