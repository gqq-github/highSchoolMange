package cn.gq.highschoolmanger.utils;

import android.text.TextUtils;

public class LoginUtils {
    public static  boolean passwordIsValidate (String password) {
        if(TextUtils.isEmpty(password)) {
            return  false ;
        }
       return checkPassword(password);
    }
    public static boolean checkPassword (String password) {
        return true ;
    }

}
