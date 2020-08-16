package cn.gq.highschoolmanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.entity.Course;

// 设置获取的课程的动态适配器
public class Courser_Adapter extends BaseAdapter {
    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }

     List<Course> list;
     LayoutInflater inflater;
     public Courser_Adapter (Context context) {
         this.inflater =LayoutInflater.from(context) ;
     }
    @Override
    public int getCount() {
        return list==null? 0: list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
CourseHolder courseHolder = null  ;
  if(convertView==null) {
    convertView = inflater.inflate(R.layout.course_item,null);
    courseHolder = new CourseHolder();
    courseHolder.showWord = (TextView)convertView.findViewById(R.id.tv_course_item);
    convertView.setTag(courseHolder);
  }else {
   courseHolder = (CourseHolder) convertView.getTag();
  }
     Course course = list.get(position) ;
     courseHolder.showWord.setText(course.toString());
     return convertView ;
     }
}
class CourseHolder  {
  public TextView showWord ;
}