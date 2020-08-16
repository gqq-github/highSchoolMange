package cn.gq.highschoolmanger.utils;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class HeaderUtil {
 public static RequestParams setValueToRequestParams (Map<String,Object> map) {
     RequestParams requestParams = new RequestParams();
     for (String key:map.keySet()) {
         requestParams.put(key,map.get(key));
     }
     return  requestParams ;
    }

    public  static <T> RequestParams  getRequestParamsByFileName(T t,List<String> filedNames )  {
        RequestParams requestParams = new RequestParams();
        for (int i = 0; i < filedNames.size() ; i++) {
            String temp = filedNames.get(i)  ;
            requestParams.put(temp,ReflectUtil.getFieldValue(t,temp));
        }
        return requestParams ;
    }
   public static void setRequestParamByListView (RequestParams param,String key , String Value ) {
     param.put(key,Value);
   }
    public static void setHeader (AppCompatActivity activity) {
     HttpClient.setHeaders(LOCAL_USERINFO.getUserInfo(activity));
    }


    public static RequestParams getRequestParamsFromObject(Object obj) {
        RequestParams params = new RequestParams();
        Class classType = obj.getClass();
        Field[] fields = classType.getDeclaredFields();
        if (fields != null) {
            int length = fields.length;
            for (int i = 0; i < length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Method getMethod = classType.getMethod(getMethodName,
                            new Class[] {});
                    Object value = getMethod.invoke(obj, new Object[] {});
                    if (value instanceof File) {
                        try {
                            params.put(fieldName, (File) value);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        params.put(fieldName,
                                value != null ? String.valueOf(value)
                                        : (String) null);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return params;
    }
}
