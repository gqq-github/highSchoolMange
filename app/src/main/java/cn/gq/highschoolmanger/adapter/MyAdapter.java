package cn.gq.highschoolmanger.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cn.gq.highschoolmanger.handler.IMyDo;
import cn.gq.highschoolmanger.handler.MY_SYNHandler;
import cn.gq.highschoolmanger.handler.ResponseJsonHandle;
import cn.gq.highschoolmanger.utils.HttpClient;


public abstract class MyAdapter<T> extends BaseAdapter {
    private ArrayList<T> mData;
    // 专门用来保存EditText返回的数据
    private int mLayoutRes;//布局id
    // Adapter中的position 记录
    public MyAdapter(ArrayList<T> mData, int mLayoutRes) {
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }

    public ArrayList<T> getEditData() {
        return mData;
    }


    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView, parent, mLayoutRes
                , position);

        bindView(holder,getItem(position));

        return holder.getItemView();
    }

    public abstract void bindView(ViewHolder holder, T obj);

    //添加一个元素
    public void add(T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }
    public void add(ArrayList<T> datas) {

        mData = datas ;
        notifyDataSetChanged();
    }
    //往特定位置，添加一个元素
    public void add(int position, T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }
    public void SyNNotifyDataSetChanged (MY_SYNHandler handler, IMyDo<T> iMyDo )  {
        handler.setAdapter(this);
        handler.setiMyDo(iMyDo);
    }
    public void doGetRequest ( RequestParams requestParams, ResponseJsonHandle handler) {
        HttpClient.doGet(requestPath(),requestParams,handler) ;
    }
    public void  doPostRequest (RequestParams requestParams, ResponseJsonHandle handler) {
         HttpClient.doPost(requestPath(),requestParams,handler) ;
    }
    public abstract  String  requestPath();
}


