package net.learnpark.app.teacher.learnpark.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.learnpark.app.teacher.learnpark.DianmingActivity;
import net.learnpark.app.teacher.learnpark.ManualActivity;
import net.learnpark.app.teacher.learnpark.R;
import net.learnpark.app.teacher.learnpark.shixian.Course;
import net.learnpark.app.teacher.learnpark.util.CourseUtil;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentA extends Fragment implements OnClickListener {

	private String filepath = Environment.getExternalStorageDirectory()
			.getPath() + "/learnpark/user/user.png";

	private Button mLeft = null;
	private Button mRight = null;
	private ViewPager xuankeViewPager = null;
	private int PAGER_NUM = 8;// 10个页面
	private int mCurrentViewID = 0; // 当前页面
	private List<View> mListViews = null;
	private List<Course> list;// 存放课程信息
	private YLeiPageAdapter madapter;
	private View view;
	private SharedPreferences sp;
	private String username = "张老师";
	private String classname;
	private String coursename;
	private String usermail;
	private int day;

	private int thisItem=0;
	private int todaysum;
	private int tomorrowsum;
	
	private TextView tv_teachername;
	private TextView tv_today;
	private TextView tv_tomorrow; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab1, container, false);

		// 获得当前时间信息
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK);// 当前星期几
		if (day == 1) {
			day = 7;
		} else {
			day = day - 1;
		}
		// TODO 这里还可以自动选择当前要上的课，未完成
		// int mHour=calendar.get(Calendar.HOUR_OF_DAY);
		// int mminute=calendar.get(Calendar.MINUTE);

		// 首先初始化，首页显示的信息
		tv_teachername = (TextView) view
				.findViewById(R.id.tab1_textview_nicheng);
		tv_today = (TextView) view
				.findViewById(R.id.tab1_textview_jinri);
		tv_tomorrow = (TextView) view
				.findViewById(R.id.tab1_textview_mingri);

		todaysum = 0;
		tomorrowsum = 0;
		GetCourseSum(day);

		ImageView user_photo = (ImageView) view.findViewById(R.id.image_user);
		if (BitmapFactory.decodeFile(filepath) != null) {
			user_photo.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}

		// 获得老师的登陆信息，如果没登陆，不能使用自动签到功能
		sp = getActivity()
				.getSharedPreferences("setting", Context.MODE_PRIVATE);
		updateUserMessages();
		tv_today.setText(todaysum + "节");
		tv_tomorrow.setText(tomorrowsum + "节");

		xuankeViewPager = (ViewPager) view.findViewById(R.id.xuankeviewpager);
		mLeft = (Button) view.findViewById(R.id.tab1_xuanke_left);
		mRight = (Button) view.findViewById(R.id.tab1_xuanke_right);
		mLeft.setOnClickListener(mOnClickListener);
		mRight.setOnClickListener(mOnClickListener);
		mListViews = new ArrayList<View>();
		new MyPagerView(getActivity());

		madapter = new YLeiPageAdapter(mListViews);
		xuankeViewPager.setAdapter(madapter);
		// xuankeViewPager.setCurrentItem(0); // 设置默认显示的页面
		xuankeViewPager.setOnPageChangeListener(mOnPageChangeListener);

		// 对星期按钮设置spinner事件
		final String[] data = getResources().getStringArray(
				R.array.xuankeSpinner);
		ArrayAdapter<String> xuankeAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, data);
		xuankeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner xuankeSpinner = (Spinner) view
				.findViewById(R.id.spinner_xuanke);
		xuankeSpinner.setAdapter(xuankeAdapter);
		xuankeSpinner.setSelection(day - 1); // 设置当前星期内的课程
		// xuankeSpinner.setVisibility(View.VISIBLE);
		// 设置监听事件
		xuankeSpinner
				.setOnItemSelectedListener(new SpinneronSelectedListener());

		// 点名按钮的点击事件
		Button btn = (Button) view.findViewById(R.id.tab1_button_dianming);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (usermail.length() == 0) {
					Toast.makeText(getActivity(), "您尚未登陆，无法点名，请先登陆",
							Toast.LENGTH_SHORT).show();
				} else if (classname.equals("") || coursename.equals("")) {
					Toast.makeText(getActivity(), "您尚未选择班级，无法开始点名",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getActivity(),
							DianmingActivity.class);
					intent.putExtra("classname", classname);
					intent.putExtra("coursename", coursename);
					startActivity(intent);

				}
			}
		});

		Button btnnButton = (Button) view
				.findViewById(R.id.tab1_button_dianming1);
		btnnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (classname.equals("") || coursename.equals("")) {
					Toast.makeText(getActivity(), "您尚未选择班级，无法开始点名",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getActivity(),
							ManualActivity.class);
					intent.putExtra("classname", classname);
					intent.putExtra("coursename", coursename);
					startActivity(intent);
				}
			}
		});

		return view;
	}

	// Spinner监听
	class SpinneronSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			thisItem=position;
			spinnerItemChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	// 添加viewpager中的内容
	class MyPagerView extends View {
		private Context mContext;
		List<Course> mlist;

		// private View mLayout = null;
		// private TextView mtextView_kecheng = null;
		// private TextView mtextView_didian = null;
		// private TextView mtextView_banji = null;

		public MyPagerView(Context context) {
			super(context);
			mContext = context;
			initLayout();
		}

		public MyPagerView(Context context, List<Course> list) {
			super(context);
			mlist = list;
			mContext = context;
			initLayout();
		}

		private void initLayout() {
			if (mlist != null && mlist.size() > 0) {
				for (Course course : mlist) {
					View mLayout = (View) LayoutInflater.from(mContext)
							.inflate(R.layout.view_layout, null);
					TextView mtextView_kecheng = (TextView) mLayout
							.findViewById(R.id.tab1_textview_kecheng);
					TextView mtextView_shijian = (TextView) mLayout
							.findViewById(R.id.tab1_textview_shijian);
					TextView mtextView_didian = (TextView) mLayout
							.findViewById(R.id.tab1_textview_didian);
					TextView mtextView_banji = (TextView) mLayout
							.findViewById(R.id.tab1_textview_banji);
					Log.d("TAG",
							"getCoursename" + "  " + course.getCoursename());
					mtextView_kecheng.setText(course.getCoursename());
					mtextView_shijian.setText(course.getTimebegin() + "-"
							+ course.getTimeend());
					mtextView_didian.setText(course.getCite());
					mtextView_banji.setText(course.getClassname());
					mListViews.add(mLayout);
				}
				// Log.d("TAG", "mListViews" + "  " + mListViews.size());
				madapter = new YLeiPageAdapter(mListViews);
				xuankeViewPager.setAdapter(madapter);
				madapter.notifyDataSetChanged();
				coursename = list.get(0).getCoursename();
				classname = list.get(0).getClassname();
			} else {
				View mLayout = (View) LayoutInflater.from(mContext).inflate(
						R.layout.view_layout, null);
				TextView mtextView_kecheng = (TextView) mLayout
						.findViewById(R.id.tab1_textview_kecheng);
				TextView mtextView_shijian = (TextView) mLayout
						.findViewById(R.id.tab1_textview_shijian);
				TextView mtextView_didian = (TextView) mLayout
						.findViewById(R.id.tab1_textview_didian);
				TextView mtextView_banji = (TextView) mLayout
						.findViewById(R.id.tab1_textview_banji);
				mtextView_kecheng.setText("");
				mtextView_shijian.setText("");
				mtextView_didian.setText("");
				mtextView_banji.setText("");
				mListViews.add(mLayout);
				madapter = new YLeiPageAdapter(mListViews);
				xuankeViewPager.setAdapter(madapter);
				madapter.notifyDataSetChanged();
				coursename = "";
				classname = "";
			}
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tab1_xuanke_left:
				if (mCurrentViewID != 0) {
					mCurrentViewID--;
					xuankeViewPager.setCurrentItem(mCurrentViewID, true);
				}
				break;
			case R.id.tab1_xuanke_right:
				if (mCurrentViewID != PAGER_NUM - 1) {
					mCurrentViewID++;
					xuankeViewPager.setCurrentItem(mCurrentViewID, true);
				}
				break;
			}

		}
	};

	// viewpager中的适配器
	class YLeiPageAdapter extends PagerAdapter {

		private List<View> mListViews;

		public YLeiPageAdapter(List<View> list) {
			this.mListViews = list;
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			// Log.d(TAG, "[YLeiPageAdapter]----->instantiateItem");
			((ViewPager) collection).addView(mListViews.get(position));
			return mListViews.get(position);

		}

		// @Override
		// public int getItemPosition(Object object) {
		// // TODO Auto-generated method stub
		// return POSITION_NONE;
		//
		// }

		@Override
		public int getCount() {
			// Log.d(TAG,"[YLeiPageAdapter]----->getCount");

			if (mListViews != null) {
				return mListViews.size();
			} else {
				return 0;
			}

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// Log.d(TAG, "[YLeiPageAdapter]----->isViewFromObject");
			return view == object;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			// Log.d(TAG, "[YLeiPageAdapter]----->destroyItem");
			((ViewPager) collection).removeView(mListViews.get(position));

		}
	}

	// 设置在选课时的效果
	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int currentID) {
			mCurrentViewID = currentID;
			coursename = list.get(currentID).getCoursename();
			classname = list.get(currentID).getClassname();
		}
	};

	@Override
	public void onClick(View arg0) {

	}

	// 获得今天，明天的课程总数
	public void GetCourseSum(int thisday) {

		List<Course> course_list1=CourseUtil.getCoursesListByDay(getActivity(), thisday);
		for (Course course:course_list1) {
			if (!course.getCoursename().equals("")) {
				todaysum = todaysum + 1;
			}
		}
		
		int tomorrowday;
		if (thisday == 7) {
			tomorrowday = 1;
		} else {
			tomorrowday = thisday + 1;
		}

		
		List<Course> course_list2=CourseUtil.getCoursesListByDay(getActivity(), tomorrowday);
		for (Course course:course_list2) {
			if (!course.getCoursename().equals("")) {
				tomorrowsum = tomorrowsum + 1;
			}
		}
	}
	
	public void spinnerItemChanged(){

		list = new ArrayList<Course>();
		List<Course> course_list = CourseUtil.getCoursesListByDay(
				getActivity(), thisItem + 1);
		for (Course course : course_list) {
			if (course.getClassname() != null
					&& course.getClassname().length() > 0) {
				list.add(course);
				// Log.d("TAG",
				// "list.size()" + "  " + list.size()
				// + course.getClassname() + 111);
			}
		}
		if (list != null && list.size() > 0) {
			PAGER_NUM = list.size();
			/*
			 * for (int i = 0; i < mListViews.size(); i++) {
			 * mListViews.remove(i); }
			 */
			// Log.d("TAG", "list.size()"+"  "+list.size());
			mListViews = new ArrayList<View>();
			new MyPagerView(getActivity(), list);
		} else {
			PAGER_NUM = 1;
			// Log.d("TAG", "lkjkljkl" + "  " + "no null");
			mListViews = new ArrayList<View>();
			new MyPagerView(getActivity(), list);
		}
	
	}
	
	public void updateCourseNum(){
		todaysum = 0;
		tomorrowsum = 0;
		Log.d("TAG", day+"  "+55555);
		GetCourseSum(day);
		Log.d("TAG", todaysum+" todaysum "+55555);
		Log.d("TAG", tomorrowsum+" tomorrowsum "+55555);
		tv_today.setText(todaysum + "节");
		tv_tomorrow.setText(tomorrowsum + "节");
		spinnerItemChanged();

	}
	
	public void updateUserMessages(){
		username = sp.getString("user_name", "");
		usermail = sp.getString("user_mail", "");

		tv_teachername.setText(username);
	}
	
}