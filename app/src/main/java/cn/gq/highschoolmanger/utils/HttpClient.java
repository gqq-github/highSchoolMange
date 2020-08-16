package cn.gq.highschoolmanger.utils;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.util.Map;

import cn.gq.highschoolmanger.MainActivity;
import cn.gq.highschoolmanger.config.ServerInfo;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;



public class HttpClient  {
    private static final String TAG = "HttpClient";
    //contentType
   // private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    //创建实例
    private static final AsyncHttpClient ASYNC_CLIENT = new AsyncHttpClient();

    private static Header[] headers ;
    //可在此处配置header，例如机型，版本号，umengType等
    static {
        ASYNC_CLIENT.setTimeout(20000);
    }

    //get方法
    //ResponseHandler 是TextHttpResponseHandler的子类，下面会有介绍
    public static RequestHandle doGet(String relativeUrl, RequestParams params, ResponseJsonHandle responseHandler) {

        params = addCommonParams(params);
        //完整url
        String url = getAbsolutUrl(relativeUrl);
        //在此处添加网络监测
        if (NetUtils.isNetworkConnected(MainActivity.getContext())) {
            return ASYNC_CLIENT.get(null, url, getCommonHeader(), params, responseHandler);
        } else {
            Log.v(TAG, ">> 网络连接异常");
            onNetDisabled(responseHandler);
            return null;
        }

    }

    public static  RequestHandle doAdd (String relativeUrl,HttpEntity entity,ResponseJsonHandle handle) {
        String url = getAbsolutUrl(relativeUrl);
        if(NetUtils.isNetworkConnected(MainActivity.getContext())) {
            return ASYNC_CLIENT.put(null, url, getCommonHeader(), entity, RequestParams.APPLICATION_JSON, handle);
        }
        else  {
            Log.v(TAG, ">> 网络连接异常");
            onNetDisabled(handle);
            return null;
        }
    }
    public static  RequestHandle doUpdate (String relativeUrl,RequestParams params,ResponseJsonHandle handle) throws IOException {
        params = addCommonParams(params) ;
        String url = getAbsolutUrl(relativeUrl);
        if(NetUtils.isNetworkConnected(MainActivity.getContext())) {

            return ASYNC_CLIENT.patch(null,url,getCommonHeader(),params.getEntity(handle),RequestParams.APPLICATION_JSON,handle) ;
        }
        else {
            Log.v(TAG, ">> 网络连接异常");
            onNetDisabled(handle);
            return null;
        }
    }

    public static RequestHandle doPost(String relativeUrl, RequestParams params, ResponseJsonHandle handle) {
        params = addCommonParams(params);
        String url = getAbsolutUrl(relativeUrl);
        if (NetUtils.isNetworkConnected(MainActivity.getContext())) {
            return ASYNC_CLIENT.post(null, url, getCommonHeader(), params, RequestParams.APPLICATION_JSON, handle);
        } else {
            Log.v(TAG, ">> 网络连接异常");
            onNetDisabled(handle);
            return null;
        }

    }

     //实现文件的上传
     // file 参数可以不传
    public static RequestHandle doPost(String file,String relativeUrl, RequestParams params, ResponseJsonHandle handle) {
        params = addCommonParams(params,file);
        String url = getAbsolutUrl(relativeUrl);
        if (NetUtils.isNetworkConnected(MainActivity.getContext())) {
           // return ASYNC_CLIENT.post( url, params, handle);
            return ASYNC_CLIENT.post(null, url, getCommonHeader(), params, null, handle);
        } else {
            Log.v(TAG, ">> 网络连接异常");
            onNetDisabled(handle);
            return null;
        }

    }


    // 其实是设计头信息
    public static Header[] getCommonHeader() {
//        Header[] headers = new BasicHeader[1];
//        headers[0] = new BasicHeader("Content-type","application/json");return headers;
       return  headers ;
    }
    // 设置头部信息
  public static void setHeaders (Map<String ,String> map) {
        if (map==null||headers!=null){
            return;
        }
        int new_length = map.size()+(headers==null?0:headers.length) ;
        Header [] tempHeader = new BasicHeader[new_length];
        int i =0 ;
        for (String key : map.keySet()) {
                 tempHeader[i] = new BasicHeader(key,map.get(key));
                 i++;
         }
        for (int j = i ; j<new_length ; j++) {
                 tempHeader[j] = headers[new_length-i-1] ;
        }
        headers = null ;
        headers = tempHeader ;
        tempHeader = null;
  }
    //无网络
    private static void onNetDisabled(ResponseJsonHandle responseHandler) {
        responseHandler.onStart();
        responseHandler.onFailure(0,"网络请求异常无网络");
        responseHandler.onFinish();
    }

    //完整url路径
    private static String getAbsolutUrl(String relativeUrl) {
        if (TextUtils.isEmpty(relativeUrl)) {
            return ServerInfo.getServerAddress();
        }
        return ServerInfo.getServerAddress() + relativeUrl;
    }

    //参数
    private static RequestParams addCommonParams(RequestParams params) {
        if (params == null) {
            params = new RequestParams();
        }
        params.setUseJsonStreamer(true);
        return params;
    }
    private static RequestParams addCommonParams(RequestParams params,String file) {
        if (params == null) {
            params = new RequestParams();
        }
        params.setForceMultipartEntityContentType(true);
        return params;
    }
}