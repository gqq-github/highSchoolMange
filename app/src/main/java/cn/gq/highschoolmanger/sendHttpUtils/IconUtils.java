package cn.gq.highschoolmanger.sendHttpUtils;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.utils.HttpClient;

public class IconUtils {
    public static RequestHandle loadCourseData (RequestParams requestParams, ResponseJsonHandle handler) {
        return  HttpClient.doGet("main/contentIcon",requestParams,handler) ;
    }
}
