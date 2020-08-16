package cn.gq.highschoolmanger.handler;

import android.os.Message;

import cn.gq.highschoolmanger.adapter.MyAdapter;

public interface IMyDo<T> {
    void bindMessageAndAdapter (Message msg, MyAdapter<T> adapter);
}