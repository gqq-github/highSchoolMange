package cn.gq.highschoolmanger.homepage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cn.gq.highschoolmanger.R;

// 这个表示通知的模块
// 根据用户角色获取相应的视图
public class ZhidaoFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    View view = inflater.inflate(R.layout.main_tab1_fragment, container,
					false);
		return view;
	}

}
