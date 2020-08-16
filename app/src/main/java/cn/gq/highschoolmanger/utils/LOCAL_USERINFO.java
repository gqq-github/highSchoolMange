package cn.gq.highschoolmanger.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class LOCAL_USERINFO {
    private static String USER_INFO  = "USERINFO" ;
    @SuppressLint("CommitPrefEdits")
    public static void saveUSER (Context con, String userId,String role, String token) {
        SharedPreferences sharedPreferences = con.getSharedPreferences(USER_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("Authorization", token);
        edit.putString("role", role);
        edit.putString("userId",userId) ;
        edit.commit();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> getUserInfo (Context con) {
        SharedPreferences sharedPreferences = con.getSharedPreferences(USER_INFO, Activity.MODE_PRIVATE);
            Map<String ,String>   userInfoMap = new HashMap<>(3) ;
            String token = sharedPreferences.getString("Authorization", "Authorization");
            String role = sharedPreferences.getString("role", "role");
            String userId = sharedPreferences.getString("userId","userId");
        userInfoMap.put("userId",userId) ;
        userInfoMap.put("Authorization",token) ;
        userInfoMap.put("role",role) ;
        return  userInfoMap;
     }
      // 调用该方法的时候表示用户出现了401要重新登录
    public static void delUserInfo   (Context con) {
        SharedPreferences sharedPreferences = con.getSharedPreferences(USER_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        System.out.println( edit.clear().commit());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }
