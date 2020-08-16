package cn.gq.highschoolmanger.login;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.utils.HttpClient;

public class LoginApi {
   public static RequestHandle test (ResponseJsonHandle handler) {
       return HttpClient.doGet("demo/demo",null,handler);
   }
   public static RequestHandle postRegister (RequestParams requestParams, ResponseJsonHandle handler) {
       return  HttpClient.doPost("user/register",requestParams,handler) ;
   }
    public static RequestHandle postLogin (RequestParams requestParams, ResponseJsonHandle handler) {
        return  HttpClient.doPost("user/login",requestParams,handler) ;
    }
}
