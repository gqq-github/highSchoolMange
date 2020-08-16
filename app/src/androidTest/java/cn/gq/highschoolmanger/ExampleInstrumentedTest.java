//package cn.gq.highschoolmanger;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static junit.framework.TestCase.assertEquals;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//    public static final String TAG = "test" ;
//    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        SharedPreferences userInfo = appContext.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor edit = userInfo.edit();
//        edit.putString("token","123fafds");
//        edit.commit() ;
//
//     //    assertEquals("cn.gq.highschoolmanger", appContext.getPackageName());
//        assertEquals("123fafds",getUserInfo("token"));
//    }
//    public String getUserInfo (String key) {
//        SharedPreferences userInfo = appContext.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//        String token = userInfo.getString(key, null);
//        Log.i(TAG, "getUserInfo: " +token);
//        return  token ;
//    }
//}
