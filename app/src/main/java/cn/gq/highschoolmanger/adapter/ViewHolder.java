package cn.gq.highschoolmanger.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gq.highschoolmanger.entity.EditEntity;

public  class ViewHolder {
    private SparseArray<View> mViews;   //存储ListView 的 item中的View
    private View item;                  //存放convertView
    private int position;               //游标
    private Context context;            //Context上下文

    //构造方法，完成相关初始化
    private ViewHolder(Context context, ViewGroup parent, int layoutRes) {
        mViews = new SparseArray<>();
        this.context = context;
        View convertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        convertView.setTag(this);
        item = convertView;
    }

    //绑定ViewHolder与item
    public static ViewHolder bind(Context context, View convertView, ViewGroup parent,
                                  int layoutRes, int position) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, parent, layoutRes);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.item = convertView;
        }
        holder.position = position;

        return holder;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T t = (T) mViews.get(id);
        if (t == null) {
            t = (T) item.findViewById(id);
            mViews.put(id, t);
        }
        return t;
    }


    /**
     * 获取当前条目
     */
    public View getItemView() {
        return item;
    }

    /**
     * 获取条目位置
     */
    public int getItemPosition() {
        return position;
    }

    /**
     * 设置文字
     */
    public ViewHolder setText(int id, CharSequence text) {
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }
    public ViewHolder setEditText(int id,Object o) {
        View view = getView(id);
        final EditEntity entity = (EditEntity) o;
        if(view instanceof EditText) {
            EditText text = (EditText) view;
            if(text.getTag() instanceof TextWatcher) {
                text.removeTextChangedListener((TextWatcher) text.getTag());
            }
            text.setText(entity.responseValue);
            text.setHint(entity.editName);
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                      if(TextUtils.isEmpty(s)) {
                          entity.setResponseValue("");
                      }else {
                          entity.setResponseValue(s.toString());
                      }
                }
            } ;
            text.addTextChangedListener(watcher);
            text.setTag(watcher);
            return this ;
        }
        return this;
    }
    /**
     * 设置图片
     */
    public ViewHolder setImageResource(int id, int drawableRes) {
        View view = getView(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(drawableRes);
        } else {
            view.setBackgroundResource(drawableRes);
        }
        return this;
    }
    public ViewHolder setImageResource(int id,String base64) {
        View view = getView(id);
        if (view instanceof ImageView) {
            byte[] decode = Base64.decode(base64, Base64.DEFAULT);
            Bitmap  decodeByteArray = BitmapFactory.decodeByteArray(decode,0,decode.length) ;
            ( (ImageView)view).setImageBitmap(decodeByteArray);
        }
        return this;
    }
   public ViewHolder setImageResource (int id , Bitmap bitmap) {
       View view = getView(id);
       if (view instanceof ImageView) {
           ( (ImageView)view).setImageBitmap(bitmap);
       }
       return this;
   }
    /**
     * 设置点击监听
     */
    public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
        getView(id).setOnClickListener(listener);
        return this;
    }

    /**
     * 设置可见
     */
    public ViewHolder setVisibility(int id, int visible) {
        getView(id).setVisibility(visible);
        return this;
    }

    /**
     * 设置标签
     */
    public ViewHolder setTag(int id, Object obj) {
        getView(id).setTag(obj);
        return this;
    }

    //其他方法可自行扩展

}

