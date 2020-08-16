package cn.gq.highschoolmanger.utils;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EidTextUtils {
    // 获取相应的标签里面的值
    public static<T extends AppCompatActivity>  String getStringVlue (T t,int id) {
        return ((EditText)t.findViewById(id)).getText().toString();
    }
    public static<T extends AppCompatActivity>  String getStringVlue (View view, int id) {
        return  ((EditText)view.findViewById(id)).getText().toString() ;
    }
}
