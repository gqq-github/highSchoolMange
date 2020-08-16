package me.kareluo.imaging.widget;



import me.kareluo.imaging.IMGGalleryActivity;

/**
 * Created by felix on 2018/1/4 下午3:53.
 */

public interface IMGGalleryHolderCallback extends IMGViewHolderCallback {

    void onCheckClick(IMGGalleryActivity.ImageViewHolder holder);
}
