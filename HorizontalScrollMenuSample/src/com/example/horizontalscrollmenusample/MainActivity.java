package com.example.horizontalscrollmenusample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lynnchurch.horizontalscrollmenu.BaseAdapter;
import com.lynnchurch.horizontalscrollmenu.HorizontalScrollMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private HorizontalScrollMenu hsm_container;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	public void initView()
	{
		hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
		hsm_container.setAdapter(new MenuAdapter());
	}

	class MenuAdapter extends BaseAdapter
	{
		String[] names = new String[]
		{ "菜单一", "菜单二", "菜单三", "菜单四", "菜单五", "菜单六", "菜单七" };

		@Override
		public List<String> getMenuItems()
		{
			// TODO Auto-generated method stub
			return Arrays.asList(names);
		}

		@Override
		public List<View> getContentViews()
		{
			// TODO Auto-generated method stub
			List<View> views = new ArrayList<View>();
			for (String str : names)
			{
				View v = LayoutInflater.from(MainActivity.this).inflate(
						R.layout.content_view, null);
				TextView tv = (TextView) v.findViewById(R.id.tv_content);
				tv.setText(str);
				views.add(v);
			}
			return views;
		}

		@Override
		public void onPageChanged(int position, boolean visitStatus)
		{
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this,
					"内容页：" + (position + 1) + " 访问状态：" + visitStatus,
					Toast.LENGTH_SHORT).show();
		}

	}
}
