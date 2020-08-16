package cn.gq.highschoolmanger.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import cn.gq.highschoolmanger.adapter.MyAdapter;

public class MY_SYNHandler extends Handler {

    public MY_SYNHandler() {
        super();
    }
    public IMyDo getiMyDo() {
        return iMyDo;
    }

    public void setiMyDo(IMyDo iMyDo) {
        this.iMyDo = iMyDo;
    }

    private IMyDo iMyDo ;

    public MyAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
    }

    private MyAdapter adapter ;

    @Override
    public void  handleMessage(@NonNull Message msg) {

        iMyDo.bindMessageAndAdapter(msg,adapter);

    }

}