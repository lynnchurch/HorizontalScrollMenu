package com.lynnchurch.horizontalscrollmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 横向滑动的菜单
 * 
 * @author LynnChurch
 * @version 创建时间:2015年6月24日 下午4:33:35
 * 
 */
public class HorizontalScrollMenu extends LinearLayout
{
	private BaseAdapter mAdapter;
	private RadioGroup rg_items;
	private List<RadioButton> rb_items = new ArrayList<RadioButton>();
	private ViewPager vp_content;
	private Context mContext;
	private ColorStateList mColors;
	private int mBackgroundResId;
	private int mPaddingLeft = 20;
	private int mPaddingTop = 20;
	private int mPaddingRight = 20;
	private int mPaddingBottom = 20;
	private HorizontalScrollView hsv_menu;
	private boolean[] mVisitStatus; // 菜单访问状态

	public HorizontalScrollMenu(Context context)
	{
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollMenu(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollMenu(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		View v = LayoutInflater.from(context).inflate(
				R.layout.horizontal_scroll_menu, this, true);
		rg_items = (RadioGroup) v.findViewById(R.id.rg_items);
		vp_content = (ViewPager) v.findViewById(R.id.vp_content);
		mColors = getResources().getColorStateList(
				R.drawable.selector_menu_item_text);
		hsv_menu = (HorizontalScrollView) v.findViewById(R.id.hsv_menu);
		mBackgroundResId = R.drawable.bg_rb_checked;
	}

	public void setAdapter(BaseAdapter adapter)
	{
		mAdapter = adapter;
		List<String> items = mAdapter.getMenuItems();
		mVisitStatus = new boolean[items.size()];
		initMenuItems(items);
		initContentViews(mAdapter.getContentViews());
	}

	/**
	 * 初始化菜单项
	 * 
	 * @param items
	 */
	public void initMenuItems(List<String> items)
	{
		if (null != items && 0 != items.size())
		{
			rg_items.setOnCheckedChangeListener(mItemListener);
			for (String str : items)
			{
				RadioButton rb_item = (RadioButton) LayoutInflater.from(
						mContext).inflate(R.layout.menu_item, null);
				rb_item.setTextColor(mColors);
				rb_item.setText(str);
				rb_item.setGravity(Gravity.CENTER);
				rb_item.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight,
						mPaddingBottom);
				rg_items.addView(rb_item);
				rb_items.add(rb_item);
			}
			rb_items.get(0).setChecked(true);
		}

	}

	/**
	 * 初始化内容
	 * 
	 * @param contentViews
	 */
	public void initContentViews(List<View> contentViews)
	{
		if (null == contentViews || 0 == contentViews.size())
		{
			return;
		}
		vp_content.setAdapter(new MyViewPager(contentViews));
		vp_content.setOnPageChangeListener(mPageListener);
	}

	/**
	 * 设置颜色变化列表
	 * 
	 * @param colorListId
	 */
	public void setColorList(int colorListId)
	{
		mColors = getResources().getColorStateList(colorListId);
	}

	/**
	 * 设置菜单项状态背景
	 * 
	 * @param resId
	 */
	public void setCheckedBackground(int resId)
	{
		mBackgroundResId = resId;
	}

	/**
	 * 菜单项切换监听器
	 */
	private OnCheckedChangeListener mItemListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			// TODO Auto-generated method stub
			RadioButton btn = (RadioButton) group.findViewById(checkedId);
			setMenuItemsNullBackground();
			btn.setBackgroundResource(mBackgroundResId);
			btn.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight,
					mPaddingBottom);
			int position = 0;
			for (int i = 0; i < rb_items.size(); i++)
			{
				if (rb_items.get(i) == btn)
				{
					position = i;
				}
			}
			vp_content.setCurrentItem(position);
			moveItemToCenter(btn);
			mAdapter.onPageChanged(position, mVisitStatus[position]);
			mVisitStatus[position] = true;
		}

	};

	/**
	 * 内容页切换监听器
	 */
	private OnPageChangeListener mPageListener = new OnPageChangeListener()
	{

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
			rb_items.get(arg0).setChecked(true);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 将菜单项尽量移至中央位置
	 * 
	 * @param rb
	 */
	private void moveItemToCenter(RadioButton rb)
	{
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int[] locations = new int[2];
		rb.getLocationInWindow(locations);
		int rbWidth = rb.getWidth();
		hsv_menu.smoothScrollBy((locations[0] + rbWidth / 2 - screenWidth / 2),
				0);
	}

	/**
	 * 设置所有菜单项的背景为空
	 */
	private void setMenuItemsNullBackground()
	{
		if (null != rg_items)
			for (int i = 0; i < rg_items.getChildCount(); i++)
			{
				View v = rg_items.getChildAt(i);
				v.setBackgroundResource(android.R.color.transparent);
			}
	}

	public void setMenuItemPaddingLeft(int paddingLeft)
	{
		mPaddingLeft = paddingLeft;
	}

	public void setMenuItemPaddingTop(int paddingTop)
	{
		mPaddingTop = paddingTop;
	}

	public void setMenuItemPaddingRight(int paddingRight)
	{
		mPaddingRight = paddingRight;
	}

	public void setMenuItemPaddingBottom(int paddingBottom)
	{
		mPaddingBottom = paddingBottom;
	}

	/**
	 * 视图页的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	static class MyViewPager extends PagerAdapter
	{
		private List<View> mViews;

		public MyViewPager(List<View> views)
		{
			// TODO Auto-generated constructor stub
			mViews = views;
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			// TODO Auto-generated method stub
			container.removeView(mViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			// TODO Auto-generated method stub
			container.addView(mViews.get(position));
			return mViews.get(position);
		}
	}
}
