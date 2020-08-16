package cn.gq.highschoolmanger.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;

public class BannerX  {
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    public  BannerX ( Banner mBanner) {
        this.mBanner = mBanner ;
        initData();
        initBanner();
    }
    public void initBanner() {
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
}
class MyImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setImageResource((Integer) path);

    }

}