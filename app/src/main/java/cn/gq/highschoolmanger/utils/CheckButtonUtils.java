package cn.gq.highschoolmanger.utils;

import cn.gq.highschoolmanger.R;

public class CheckButtonUtils {
    public static  int getRole(int id) {
        int role = 0;
        switch (id) {
            case R.id.manger:
                role = 0;
                break;
            case R.id.teacher:
                role = 1;
                break;
            case R.id.student:
                role = 2;
                break;
            default:
                role = 2;
                break;
        }
        return  role ;
    }
}
