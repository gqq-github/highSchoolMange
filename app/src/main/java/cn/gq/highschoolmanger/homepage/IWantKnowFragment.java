package cn.gq.highschoolmanger.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.gq.highschoolmanger.MainActivity;
import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.adapter.MyAdapter;
import cn.gq.highschoolmanger.adapter.ViewHolder;
import cn.gq.highschoolmanger.config.MessageType;
import cn.gq.highschoolmanger.entity.Content_Icon;
import cn.gq.highschoolmanger.entity.Course;
import cn.gq.highschoolmanger.entity.MessageEntity;
import cn.gq.highschoolmanger.handler.IMyDo;
import cn.gq.highschoolmanger.handler.MY_SYNHandler;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.sendHttpUtils.CourseUtils;
import cn.gq.highschoolmanger.sendHttpUtils.IconUtils;
import cn.gq.highschoolmanger.utils.LOCAL_USERINFO;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 我想知道的碎片页面
 *
 * @author wwj_748
 */
public class IWantKnowFragment extends Fragment implements OnBannerListener {
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private MyAdapter<Course> courseMyAdapter ;
    private MyAdapter<Content_Icon> iconMyAdapter ;
    private List<Course> courses = new ArrayList<>();
     private List<Content_Icon> icons = new ArrayList<Content_Icon>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab2_fragment, container,
                false);

        ListView listView = view.findViewById(R.id.lv_course_item);
        initData();
        initBanner(view);
       initIcon(view);
       initCourse(listView);
        return view;
    }
      MY_SYNHandler my_synHandler = new MY_SYNHandler();
    MY_SYNHandler my_synHandler1 = new MY_SYNHandler();
    public void  initIcon (View view) {
        GridView gv_icon = view.findViewById(R.id.gv_icon);
        iconMyAdapter = new MyAdapter<Content_Icon>((ArrayList<Content_Icon>) icons,R.layout.iocn_item) {
            @Override
            public void bindView(ViewHolder holder, Content_Icon obj) {
                holder.setText(R.id.tv_icon,obj.iconName);
                holder.setImageResource(R.id.iv_icon,obj.base64) ;
            }

            @Override
            public String requestPath() {
                return null;
            }
        } ;
        gv_icon.setAdapter(iconMyAdapter);
        iconMyAdapter.SyNNotifyDataSetChanged(my_synHandler1, new IMyDo<Content_Icon>() {
            @Override
            public void bindMessageAndAdapter(Message msg, MyAdapter<Content_Icon> adapter) {
                MessageEntity messageEntity = (MessageEntity) msg.obj;
                if (messageEntity.message_what==1 && messageEntity.message_type.equals(MessageType.ICON_MESSAGE)) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        gv_icon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(HomePageActivity.getContext(), "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
            }
        });
        loadIconSYN();

    }
    public void loadIconSYN () {
        IconUtils.loadCourseData(null, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                my_synHandler1.sendMessage(MessageEntity.getMessage(MessageType.ICON_MESSAGE,2) );
            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {
                System.out.println(data);
                JSONArray data1 = data.getJSONArray("data");
                for (int i =0 ; i<data1.length() ; i++) {
                    Content_Icon contentIcon = new Content_Icon();
                    JSONObject json = (JSONObject) data1.get(i);
                    contentIcon.iconName = (String)json.get("iconName");
                    contentIcon.base64 = (String) json.get("image");
                    icons.add(contentIcon);
                }
                my_synHandler1.sendMessage(MessageEntity.getMessage(MessageType.ICON_MESSAGE,1) );
            }
        }) ;
    }
    public void initCourse  (ListView view) {
        courseMyAdapter = new MyAdapter<Course>((ArrayList<Course>) courses,R.layout.course_item) {
            @Override
            public void bindView(ViewHolder holder, Course obj) {
                holder.setText(R.id.tv_course_item,obj.toString());
            }

            @Override
            public String requestPath() {
                return null;
            }
        } ;
        view.setAdapter(courseMyAdapter);
        courseMyAdapter.SyNNotifyDataSetChanged(my_synHandler, new IMyDo<Course>() {
            @Override
            public void bindMessageAndAdapter(Message msg, MyAdapter<Course> adapter) {
                MessageEntity messageEntity = (MessageEntity) msg.obj;
                if (messageEntity.message_what==1 && messageEntity.message_type.equals(MessageType.COURSE_MESSAGE)) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        loadCourseItemListSYN();
    }


    // 因为请求是异步的所以直接调用封装好的方法即可
    private void loadCourseItemListSYN() {
        CourseUtils.loadCourseData(null, new ResponseJsonHandle() {
            @Override
            public void onFailure(int statusCode, String msg) {
                if(statusCode==401) {
                        LOCAL_USERINFO.delUserInfo(MainActivity.getContext());
                        //todo : 添加提示框
                        Intent intent = new Intent(HomePageActivity.getContext(), MainActivity.class);
                        startActivity(intent);

                }else {

                    my_synHandler.sendMessage(MessageEntity.getMessage(MessageType.COURSE_MESSAGE,2)) ;
                }
            }

            @Override
            protected void onSuccess(JSONObject data) throws JSONException {

                courses.add(new Course(true)) ;
                // 发送消息 通知将数据设置到adapter当中
                my_synHandler.sendMessage(MessageEntity.getMessage(MessageType.COURSE_MESSAGE,1));
            }
        });

    }

    public void initBanner(View view) {
        mBanner = view.findViewById(R.id.banner);
        mMyImageLoader = new MyImageLoader();
        mBanner.setImageLoader(mMyImageLoader);
        //设置图片集合
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        mBanner.setImages(imagePath);
        //轮播图片的文字
//        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    private void initData() {
        imagePath = new ArrayList<>();
        imagePath.add(R.drawable.banner1);
        imagePath.add(R.drawable.banner2);
        imagePath.add(R.drawable.banner3);
    }


    class MyImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource((Integer) path);

        }

    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getContext(), "点击" + position, LENGTH_SHORT).show();
    }

}


