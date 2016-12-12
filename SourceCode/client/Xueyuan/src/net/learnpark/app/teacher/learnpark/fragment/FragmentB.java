package net.learnpark.app.teacher.learnpark.fragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.learnpark.app.teacher.learnpark.R;
import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.shixian.Course;
import net.learnpark.app.teacher.learnpark.shixian.GetcourseColor;
import net.learnpark.app.teacher.learnpark.shixian.GetcourseId;
import net.learnpark.app.teacher.learnpark.util.ClassesUtil;
import net.learnpark.app.teacher.learnpark.util.CourseUtil;
import net.learnpark.app.teacher.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FragmentB extends Fragment implements OnClickListener{

	List<String> coursename_sum = new ArrayList<String>();

	
	OnCourseChangeListener mCallback;
	
	// 设置一下上午和下午什么时候开始上课
	int morningstart = 8;
	int afternonestart = 14;

	Course[][] courses = new Course[7][8];
	TextView textCourse;
	TextView[][] textCourses = new TextView[7][8];

	// 添加课程栏目里的控件
	Spinner add_week; // 星期几
	EditText add_coursename;// 课程名
	TextView add_timebegin;// 开始时间
	TextView add_timeend; // 结束时间
	EditText add_cite; // 上课地址
	EditText add_classname; // 班级名称

	// 获得afinal相关的对象,建立班级表的afinal对象
	FinalDb classDb;

	// 获取设置的课程信息
	int[][] ids = {
			{ R.id.day1_1, R.id.day1_2, R.id.day1_3, R.id.day1_4, R.id.day1_5,
					R.id.day1_6, R.id.day1_7, R.id.day1_8 },
			{ R.id.day2_1, R.id.day2_2, R.id.day2_3, R.id.day2_4, R.id.day2_5,
					R.id.day2_6, R.id.day2_7, R.id.day2_8 },
			{ R.id.day3_1, R.id.day3_2, R.id.day3_3, R.id.day3_4, R.id.day3_5,
					R.id.day3_6, R.id.day3_7, R.id.day3_8 },
			{ R.id.day4_1, R.id.day4_2, R.id.day4_3, R.id.day4_4, R.id.day4_5,
					R.id.day4_6, R.id.day4_7, R.id.day4_8 },
			{ R.id.day5_1, R.id.day5_2, R.id.day5_3, R.id.day5_4, R.id.day5_5,
					R.id.day5_6, R.id.day5_7, R.id.day5_8 },
			{ R.id.day6_1, R.id.day6_2, R.id.day6_3, R.id.day6_4, R.id.day6_5,
					R.id.day6_6, R.id.day6_7, R.id.day6_8 },
			{ R.id.day7_1, R.id.day7_2, R.id.day7_3, R.id.day7_4, R.id.day7_5,
					R.id.day7_6, R.id.day7_7, R.id.day7_8 } };
	int week;
	Calendar c = Calendar.getInstance();

	int mHour = 0;
	// int mHour=getmHour(c.get(Calendar.HOUR_OF_DAY));
	int mMinute = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab2, container, false);

		// 取得课程信息
		List<Course> list = CourseUtil.getCoursesList(getActivity());

		for (Course course : list) {
			if (!course.getTimebegin().equals("")) {
				// Log.d("TAG", course.getTimebegin() + "  " + 1000);
				courses[course.getDay() - 1][course.getTime() - 1] = course;
				// 获得所有的课程名称
				coursename_sum = new GetcourseColor().addcourse(coursename_sum,
						course.getCoursename());

			}
		}

		// 删除课程名称集合中重复的课程
		new GetcourseColor().initcourse(coursename_sum);

		// 给课程加边框
		for (int i = 0; i < textCourses.length; i++) {
			for (int j = 0; j < textCourses[i].length; j++) {
				textCourses[i][j] = (TextView) view.findViewById(ids[i][j]);
				textCourses[i][j].setOnClickListener(this);
				textCourses[i][j]
						.setBackgroundResource(R.drawable.table_course_fangkuang);
			}
		}

		// 给课程表加内容
		for (int i = 0; i < textCourses.length; i++) {
			for (int j = 0; j < textCourses[i].length; j++) {
				// 把课程填入课程表内
				if (courses[i][j] != null) {
					// Log.d("TAG", i+"  "+j);
					mSetCourse(courses[i][j], i, j);
				}
			}
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		int courseid = v.getId();
		int courseid_i = 0;
		int courseid_j = 0;
		for (int i = 0; i < textCourses.length; i++) {
			for (int j = 0; j < textCourses[i].length; j++) {
				if ((int) ids[i][j] == courseid) {
					// Log.d("TAG", i+"  "+j);
					courseid_i = i;
					courseid_j = j;
				}
			}
		}
		AlertDialog_addcurses(courseid_i, courseid_j);
	}

	// 重写对话框点击监听事件
	@SuppressLint("HandlerLeak")
	class ButtonHandler extends Handler {
		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;
			}
		}
	}

	private int getsumminutes(String timebegin, String timeend) {
		String[] mytime_begin = timebegin.split(":");
		int myhour_begin = Integer.valueOf(mytime_begin[0]);
		int myminute_begin = Integer.valueOf(mytime_begin[1]);
		// Log.d("TAG", course+"  "+7);
		// 结束时间
		String[] mytime_end = timeend.split(":");
		int myhour_end = Integer.valueOf(mytime_end[0]);
		int myminute_end = Integer.valueOf(mytime_end[1]);
		int summinute = (myhour_end * 60 + myminute_end)
				- (myhour_begin * 60 + myminute_begin);
		return summinute;
	}

	private void mSetCourse(Course course, int i, int j) {
		// 把课程填入课程表内

		// Log.d("TAG", i+"  "+j);
		textCourses[i][j].setText(CourseUtil.getCourseString(getActivity(), i
				+ 1 + "", j + 1 + ""));
		int color = new GetcourseColor().getColor(coursename_sum,
				course.getCoursename());
		textCourses[i][j].setBackgroundColor(color);
		if (getsumminutes(course.getTimebegin(), course.getTimeend()) > 70) {
			textCourses[i][j + 1].setBackgroundColor(color);
		}
	}

	// 这里将新添加的课程，班级导入到班级表中
	private void PutInClasses(String classname, String coursename) {
		Classes classes = new Classes();
		classes.setClassname(classname);
		classes.setCoursename(coursename);
		ClassesUtil.insertClasses(getActivity(), classes);
	}

	public void AlertDialog_addcurses(int courseid_i, int courseid_j) {

		// 长按编辑该课程
		// Log.d("TAG", v.getId()+"");
		// Log.d("TAG", R.id.day1_2+"");

		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(getActivity());
		bulider.setIcon(R.drawable.ic_launcher);

		View layout = (View) LayoutInflater.from(getActivity()).inflate(
				R.layout.add_course, null);
		bulider.setView(layout);

		// 找到添加课程的控件
		add_week = (Spinner) layout.findViewById(R.id.add_week);
		add_coursename = (EditText) layout.findViewById(R.id.add_coursename);
		add_timebegin = (TextView) layout.findViewById(R.id.add_timebegin);
		add_timeend = (TextView) layout.findViewById(R.id.add_timeend);
		add_cite = (EditText) layout.findViewById(R.id.add_cite);
		add_classname = (EditText) layout.findViewById(R.id.add_classname);
		// 提示信息
		add_week.setPrompt("请设置星期");

		// 初始化添加课程里面的内容
		textCourse = textCourses[courseid_i][courseid_j];
		String textcoursetext = (String) textCourse.getText();
		add_week.setSelection(courseid_i);
		// Log.d("TAG", 1+textcoursetext+88);
		String course_timebegin = null;
		String course_timeend = null;

		switch (courseid_j) {
		case 0:
			course_timebegin = "08:00";
			course_timeend = "09:50";
			break;
		case 1:
			course_timebegin = "09:00";
			course_timeend = "09:50";
			break;
		case 2:
			course_timebegin = "10:00";
			course_timeend = "11:50";
			break;
		case 3:
			course_timebegin = "11:00";
			course_timeend = "11:50";
			break;
		case 4:
			course_timebegin = "14:00";
			course_timeend = "15:50";
			break;
		case 5:
			course_timebegin = "15:00";
			course_timeend = "15:50";
			break;
		case 6:
			course_timebegin = "16:00";
			course_timeend = "17:50";
			break;
		case 7:
			course_timebegin = "17:00";
			course_timeend = "17:50";
			break;

		default:
			break;
		}
		add_timebegin.setText("上课时间:" + course_timebegin);
		add_timeend.setText("下课时间:" + course_timeend);
		if (!textcoursetext.equals("")) {
			String[] textcouStrings = textcoursetext.split("\n");
			// int course_week=courses[courseid_i][courseid_j].getDay();
			String course_name = textcouStrings[0];
			String course_cite = textcouStrings[1];
			String course_classname = textcouStrings[2];

			add_coursename.setText(course_name);

			add_cite.setText(course_cite.replace("@", ""));
			add_classname.setText(course_classname);

			bulider.setTitle("修改课程");
			// Log.d("TAG", course_week+"  "+course_name);
		}

		// 今天星期几的选择
		add_week.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView text = (TextView) view;
				String sweek = text.getText().toString();
				// Log.d("TAG", sweek+7);

				if (sweek.equals("星期一")) {
					// Log.d("TAG", "jk"+7);
					week = 1;
				} else if (sweek.equals("星期二")) {
					week = 2;
				} else if (sweek.equals("星期三")) {
					week = 3;
				} else if (sweek.equals("星期四")) {
					week = 4;
				} else if (sweek.equals("星期五")) {
					week = 5;
				} else if (sweek.equals("星期六")) {
					week = 6;
				} else {
					Log.d("TAG", "jkakdjf" + 7);
					week = 7;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 开始时间
		add_timebegin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String[] mhourString = add_timebegin.getText().toString()
						.replace("上课时间:", "").trim().split(":");
				mHour = Integer.valueOf(mhourString[0]);
				mMinute = Integer.valueOf(mhourString[1]);
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								c.setTimeInMillis(System.currentTimeMillis());
								c.set(Calendar.HOUR_OF_DAY, hourOfDay);
								c.set(Calendar.MINUTE, minute);
								c.set(Calendar.SECOND, 0); // 设为 0
								c.set(Calendar.MILLISECOND, 0); // 设为 0
								String mhourofday = null;
								String mminute = null;
								if (hourOfDay < 10) {
									mhourofday = 0 + "" + hourOfDay;
								} else {
									mhourofday = hourOfDay + "";
								}
								if (minute < 10) {
									mminute = 0 + "" + minute;
								} else {
									mminute = minute + "";
								}
								add_timebegin.setText("上课时间:" + mhourofday
										+ ":" + mminute);
							}
						}, mHour, mMinute, true).show();

			}
		});

		// 结束时间
		add_timeend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String[] mhourString = add_timeend.getText().toString()
						.replace("下课时间:", "").trim().split(":");
				mHour = Integer.valueOf(mhourString[0]);
				mMinute = Integer.valueOf(mhourString[1]);
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								c.setTimeInMillis(System.currentTimeMillis());
								c.set(Calendar.HOUR_OF_DAY, hourOfDay);
								c.set(Calendar.MINUTE, minute);
								c.set(Calendar.SECOND, 0); // 设为 0
								c.set(Calendar.MILLISECOND, 0); // 设为 0
								String mhourofday = null;
								String mminute = null;
								if (hourOfDay < 10) {
									mhourofday = 0 + "" + hourOfDay;
								} else {
									mhourofday = hourOfDay + "";
								}
								if (minute < 10) {
									mminute = 0 + "" + minute;
								} else {
									mminute = minute + "";
								}
								add_timeend.setText("下课时间:" + mhourofday + ":"
										+ mminute);
							}
						}, mHour, mMinute, true).show();
			}
		});

		// EditText edit_coursename = (EditText) layout
		// .findViewById(R.id.edit_coursename);
		// EditText edit_cite = (EditText) layout.findViewById(R.id.edit_cite);
		// EditText edit_time = (EditText) layout.findViewById(R.id.edit_time);
		//
		// edit_coursename.setText("");
		// edit_cite.setText("");
		// edit_time.setText("");

		// 添加一个确定添加按钮
		bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 添加课程
				Course course = new Course();
				// Log.d("TAG", week+"  "+7);
				String mbegintime = add_timebegin.getText().toString()
						.replace("上课时间:", "").trim();
				String mendtime = add_timeend.getText().toString()
						.replace("下课时间:", "").trim();
				// Log.d("TAG", add_timebegin.getText().toString() + "  "
				// + add_timeend.getText().toString());
				// Log.d("TAG", mbegintime + "  " + mendtime);
				// Log.d("TAG", "上课时间:"+222);
				course.setDay(week);
				course.setCoursename(add_coursename.getText().toString());
				course.setTimebegin(mbegintime);
				course.setTimeend(mendtime);
				course.setCite(add_cite.getText().toString());
				course.setClassname(add_classname.getText().toString());
				// Log.d("TAG", course + "  " + 222);

				// Log.d("TAG", course.getTimebegin() + "  " + mendtime);

				int summinute = getsumminutes(course.getTimebegin(),
						course.getTimeend());
				int id = new GetcourseId().GetmycourseId(course.getDay(),
						course.getTimebegin(), course.getTimeend());

				// Log.d("TAG",mytime_begin[0]+"  "+mytime_begin[1]+"  "+mytime_end[0]+"  "+mytime_end[1]);
				// Log.d("TAG", myhour_end + "  " + myminute_end + "   "
				// + myhour_begin + "   " + myminute_begin);
				// Log.d("TAG", summinute + "  " + id);

				if ("".equals(course.getCite()) || "".equals(course.getDay())
						|| "".equals(course.getCoursename())
						|| "".equals(course.getTimebegin())
						|| "".equals(course.getTimeend())
						|| "".equals(course.getClassname())) {
					// 添加不成功的震动提示
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "请填写完整的信息",
							Toast.LENGTH_SHORT).show();
				} else if (summinute <= 0 || summinute > 120 || id == 0) {
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "您填写的上课时间不符合要求",
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.dismiss();
					course.setDay((id - 1) / 8 + 1);
					course.setTime((id - 1) % 8 + 1);
					int return_i = CourseUtil.insertCoursesForResult(
							getActivity(), course);
					
					mCallback.onCourseChanged(0);
					if (return_i == 2) {
						// 显示刚添加的内容
						coursename_sum = new GetcourseColor().addcourse(
								coursename_sum, course.getCoursename());
						new GetcourseColor().initcourse(coursename_sum);
						mSetCourse(course, (id - 1) / 8, (id - 1) % 8);
						Toast.makeText(getActivity(), "添加成功",
								Toast.LENGTH_SHORT).show();

						PutInClasses(course.getClassname(),
								course.getCoursename());

					} else if (return_i == 1) {

						coursename_sum = new GetcourseColor().addcourse(
								coursename_sum, course.getCoursename());
						new GetcourseColor().initcourse(coursename_sum);
						// 这里还需要在继续优化一下显示问题
						mSetCourse(course, (id - 1) / 8, (id - 1) % 8);

						Toast.makeText(getActivity(), "修改成功",
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		});

		// 添加一个删除按钮
		bulider.setNeutralButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Course course = new Course();

				String mbegintime = add_timebegin.getText().toString()
						.replace("上课时间:", "").trim();
				String mendtime = add_timeend.getText().toString()
						.replace("下课时间:", "").trim();
				// Log.d("TAG", add_timebegin.getText().toString() + "  "
				// + add_timeend.getText().toString());
				// Log.d("TAG", mbegintime + "  " + mendtime);
				// Log.d("TAG", "上课时间:"+222);
				course.setDay(week);
				course.setCoursename(add_coursename.getText().toString());
				course.setTimebegin(mbegintime);
				course.setTimeend(mendtime);
				course.setCite(add_cite.getText().toString());
				course.setClassname(add_classname.getText().toString());

				int id = new GetcourseId().GetmycourseId(course.getDay(),
						course.getTimebegin(), course.getTimeend());

				if ("".equals(course.getCite()) || "".equals(course.getDay())
						|| "".equals(course.getCoursename())
						|| "".equals(course.getTimebegin())
						|| "".equals(course.getTimeend())
						|| "".equals(course.getClassname())) {
					// 添加不成功的震动提示
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "内容为空,无法删除",
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.dismiss();

					// 删除屏幕上刚删除的内容
					Log.d("TAG", "id:   " + id);
					textCourse = textCourses[(id - 1) / 8][(id - 1) % 8];
					textCourse.setText("");
					textCourse
							.setBackgroundResource(R.drawable.table_course_fangkuang);
					if (getsumminutes(course.getTimebegin(),
							course.getTimeend()) > 70) {
						textCourses[id / 8][id % 8]
								.setBackgroundResource(R.drawable.table_course_fangkuang);
					}
					course.setDay((id - 1) / 8 + 1);
					course.setTime((id - 1) % 8 + 1);
					course.setCite("");
					course.setClassname("");
					course.setCoursename("");
					course.setTimebegin("");
					course.setTimeend("");
					CourseUtil.deleteCourses(getActivity(), course);
					mCallback.onCourseChanged(0);
					Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		// 添加一个取消按钮
		bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog = bulider.create();

		// 设置点击对话框的确定按钮后对话框不消失
		try {
			Field field = alertDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(alertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, new ButtonHandler(alertDialog));
		} catch (Exception e) {
		}

		alertDialog.show();
	}

	// 实现fragment和activity之间通信
	public interface OnCourseChangeListener {
		public void onCourseChanged(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnCourseChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

}
