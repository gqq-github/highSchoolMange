package cn.gq.highschoolmanger.student;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.loading.LoadingX;

//展示学生的个人的到课情况，以图表的形式展示信息
public class ShowStudentPersonalInfo extends AppCompatActivity implements OnChartValueSelectedListener {
    private PieChart mPieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.student_person_info);
         initPieChart();
        initData();
    }

    public void initPieChart() {
        this.mPieChart = findViewById(R.id.student_PieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        // mPieChart.setCenterText(generateCenterSpannableText());
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);

        // 当数据没有显示的时候，给出提示信息。
        mPieChart.setNoDataText(getResources().getString(R.string.no_data));
        // 设置圆形的半径
        mPieChart.setTransparentCircleRadius(61f);
        // 饼状图中间可以添加文字
        mPieChart.setDrawCenterText(true);
        // 设置初始的旋转角度
        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);

        //设置数据
    }
    public void initData  () {

        LoadingX loadingX = new LoadingX(this) ;
        loadingX.setTitle("数据加载");
        loadingX.setMessage("加载中");
        BaseRequest baseRequest = new BaseRequest(loadingX) {
            @Override
            public void onFailureDo(int code) {

            }
            @Override
            public void onSuccessDo(JSONObject data) {
                try {
                    delData (data.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
         baseRequest.doGet(null,"student/showPipe");
    }
    public void  delData (JSONArray jsonArray) throws JSONException {
        if(jsonArray==null || jsonArray.length()==0)
        {
             setData(null);
             return;
        }
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0 ; i<jsonArray.length();i++){
            JSONObject object = jsonArray.getJSONObject(i) ;
            entries.add( new PieEntry((float) object.getDouble("count"),object.getString("temp")));
        }
        setData(entries);
    }
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}