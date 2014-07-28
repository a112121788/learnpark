package net.learnpark.app.learnpark;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 引导界面
 * 
 * @author 胡亚历 2014年7月8日 19:37:31
 */
public class FirstInActivity extends ActionBarActivity {

	private ViewPager mViewPager;
	private ImageView mPage0;// 第1页
	private ImageView mPage1;// 第2页
	private ImageView mPage2;// 第3页
	private ImageView mPage3;// 第4页
	private Button ok_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstin);
		getSupportActionBar().hide();
		SysApplication.getInstance().addActivity(this); 
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mViewPager.setOnPageChangeListener(new FristInOnPageChangeListener());
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		LayoutInflater inflate = LayoutInflater.from(this);
		View view1 = inflate.inflate(R.layout.activity_firstin_one, null);
		View view2 = inflate.inflate(R.layout.activity_firstin_two, null);
		View view3 = inflate.inflate(R.layout.activity_firstin_three, null);
		View view4 = inflate.inflate(R.layout.activity_firstin_four, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		FristInPagerAdapter mPagerAdapter = new FristInPagerAdapter(views);
		mViewPager.setAdapter(mPagerAdapter);
	}

	private class FristInPagerAdapter extends PagerAdapter {

		private ArrayList<View> views;

		public FristInPagerAdapter(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);

		}

	}

	private class FristInOnPageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int page) {
			// 翻页时当前page,改变当前状态园点图片
			switch (page) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.firstin_page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.firstin_page_now));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.firstin_page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.firstin_page_now));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.fistin_page));
				ok_btn.setText("进入");
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

}
