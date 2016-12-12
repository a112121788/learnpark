package net.learnpark.app.learnpark.fragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.learnpark.app.learnpark.CourseSQLiteOpenHelper;
import net.learnpark.app.learnpark.R;
import net.learnpark.app.learnpark.entity.Course;
import net.learnpark.app.learnpark.entity.GetcourseColor;
import net.learnpark.app.learnpark.entity.GetcourseId;
import net.learnpark.app.learnpark.util.VibratorUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CourseInfoFragment extends Fragment implements OnClickListener,
		OnLongClickListener {

	List<String> coursename_sum = new ArrayList<String>();

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
	EditText add_teacher;// 老师的名字
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
	int mMinute = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_info, container,
				false);

		// 取得课程信息
		// 查询数据库
		// 把信息存到courses中
		CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
				getActivity().getApplication());
		SQLiteDatabase coursesDb = courseHelper.getWritableDatabase();
		String sql = "select * from courses  order by day,timebegin";
		Cursor cursor = coursesDb.rawQuery(sql, new String[] {});
		List<Course> list = new ArrayList<Course>();
		while (cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			int day = Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("day")));
			int time = Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("time")));
			String coursename = cursor.getString(cursor
					.getColumnIndex("coursename"));
			String timebegin = cursor.getString(cursor
					.getColumnIndex("timebegin"));
			String timeend = cursor.getString(cursor.getColumnIndex("timeend"));
			String cite = cursor.getString(cursor.getColumnIndex("cite"));
			String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
			Course course = new Course(id, day, time, coursename, timebegin,
					timeend, cite, teacher);
			list.add(course);
		}
		courseHelper.close();
		// int[] coursesite = new int[list.size()];
		// Log.d("TAG",list.size()+"  "+88);
		// list存放的是所用的课程 想办法放到数组里面
		// int[] week = { 0, 0, 0, 0, 0, 0, 0 };
		for (Course course : list) {

			if (!course.getTimebegin().equals("")) {
				Log.d("TAG", course.getTimebegin() + "  " + 1000);
				courses[course.getDay() - 1][course.getTime() - 1] = course;
				// 获得所有的课程名称
				coursename_sum = new GetcourseColor().addcourse(coursename_sum,
						course.getCoursename());

			}
		}

		// 删除课程名称集合中重复的课程
		new GetcourseColor().initcourse(coursename_sum);

		for (int i = 0; i < textCourses.length; i++) {
			for (int j = 0; j < textCourses[i].length; j++) {

				textCourses[i][j] = (TextView) view.findViewById(ids[i][j]);
				textCourses[i][j].setOnLongClickListener(this);

				textCourses[i][j]
						.setBackgroundResource(R.drawable.table_course_fangkuang);
				// 把课程填入课程表内
				if (courses[i][j] != null) {
					// Log.d("TAG", i+"  "+j);
					textCourses[i][j].setText(courses[i][j].mytoString());

					textCourses[i][j].setBackgroundColor(new GetcourseColor()
							.getColor(coursename_sum,
									courses[i][j].getCoursename()));
				}
			}
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		TextView text = (TextView) v;
		Toast.makeText(getActivity(), text.getText().toString(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onLongClick(View v) {
		// 长按编辑该课程
		// Log.d("TAG", v.getId()+"");
		// Log.d("TAG", R.id.day1_2+"");
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

		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(getActivity());
		bulider.setIcon(R.drawable.ic_launcher);

		View layout = (View) LayoutInflater.from(getActivity()).inflate(
				R.layout.popup_add_course, null);
		bulider.setView(layout);

		// 找到添加课程的控件
		add_week = (Spinner) layout.findViewById(R.id.add_week);
		add_coursename = (EditText) layout.findViewById(R.id.add_coursename);
		add_timebegin = (TextView) layout.findViewById(R.id.add_timebegin);
		add_timeend = (TextView) layout.findViewById(R.id.add_timeend);
		add_cite = (EditText) layout.findViewById(R.id.add_cite);
		add_teacher = (EditText) layout.findViewById(R.id.add_teacher);
		// 提示信息
		add_week.setPrompt("请设置星期");

		// 初始化添加课程里面的内容
		textCourse = textCourses[courseid_i][courseid_j];
		String textcoursetext = (String) textCourse.getText();
		add_week.setSelection(courseid_i);
		// Log.d("TAG", 1+textcoursetext+88);
		if (!textcoursetext.equals("")) {
			String[] textcouStrings = textcoursetext.split("\n");
			// int course_week=courses[courseid_i][courseid_j].getDay();
			String course_name = textcouStrings[0];
			String course_teacher = textcouStrings[1];
			String course_timebegin = textcouStrings[2];
			String course_timeend = textcouStrings[3];
			String course_cite = textcouStrings[4];
			add_coursename.setText(course_name);
			add_timebegin.setText("上课时间:" + course_timebegin.replace("-", ""));
			add_timeend.setText("下课时间:" + course_timeend);
			add_cite.setText(course_cite);
			add_teacher.setText(course_teacher.replace("@", ""));

			bulider.setTitle("修改课程");
			// Log.d("TAG", course_week+"  "+course_name);
		} else {
			// 在时间前面加上0
			bulider.setTitle("添加课程");
			String[] mymorningstart = new String[4];
			String[] myafternonestart = new String[4];
			for (int k = 0; k < 4; k++) {
				if (morningstart + k < 10) {
					mymorningstart[k] = 0 + "" + (morningstart + k);
				} else {
					mymorningstart[k] = morningstart + k + "";
				}

				if (afternonestart + k < 10) {
					myafternonestart[k] = 0 + "" + (afternonestart + k);
				} else {
					myafternonestart[k] = afternonestart + k + "";
				}
			}

			if (courseid_j == 0) {
				add_timebegin.setText("上课时间:" + mymorningstart[0] + ":00");
				add_timeend.setText("下课时间:" + mymorningstart[1] + ":50");
			} else if (courseid_j == 1) {
				add_timebegin.setText("上课时间:" + mymorningstart[1] + ":00");
				add_timeend.setText("下课时间:" + mymorningstart[1] + ":50");
			} else if (courseid_j == 2) {
				add_timebegin.setText("上课时间:" + mymorningstart[2] + ":00");
				add_timeend.setText("下课时间:" + mymorningstart[3] + ":50");
			} else if (courseid_j == 3) {
				add_timebegin.setText("上课时间:" + mymorningstart[3] + ":00");
				add_timeend.setText("下课时间:" + mymorningstart[3] + ":50");
			} else if (courseid_j == 4) {
				add_timebegin.setText("上课时间:" + myafternonestart[0] + ":00");
				add_timeend.setText("下课时间:" + myafternonestart[1] + ":50");
			} else if (courseid_j == 5) {
				add_timebegin.setText("上课时间:" + myafternonestart[1] + ":00");
				add_timeend.setText("下课时间:" + myafternonestart[1] + ":50");
			} else if (courseid_j == 6) {
				add_timebegin.setText("上课时间:" + myafternonestart[2] + ":00");
				add_timeend.setText("下课时间:" + myafternonestart[3] + ":50");
			} else {
				add_timebegin.setText("上课时间:" + myafternonestart[3] + ":00");
				add_timeend.setText("下课时间:" + myafternonestart[3] + ":50");
			}
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
				// TODO Auto-generated method stub

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
				course.setTeacher(add_teacher.getText().toString());
				// Log.d("TAG", course + "  " + 222);

				// Log.d("TAG", course.getTimebegin() + "  " + mendtime);
				// 开始时间
				String[] mytime_begin = course.getTimebegin().split(":");
				int myhour_begin = Integer.valueOf(mytime_begin[0]);
				int myminute_begin = Integer.valueOf(mytime_begin[1]);
				// Log.d("TAG", course+"  "+7);
				// 结束时间
				String[] mytime_end = course.getTimeend().split(":");
				int myhour_end = Integer.valueOf(mytime_end[0]);
				int myminute_end = Integer.valueOf(mytime_end[1]);
				int summinute = (myhour_end * 60 + myminute_end)
						- (myhour_begin * 60 + myminute_begin);
				int id = new GetcourseId().GetmycourseId(course.getDay(),
						course.getTimebegin(), course.getTimeend());

				if ("".equals(course.getCite()) || "".equals(course.getDay())
						|| "".equals(course.getCoursename())
						|| "".equals(course.getTimebegin())
						|| "".equals(course.getTimeend())
						|| "".equals(course.getTeacher())) {
					// 添加不成功的震动提示
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "请填写完整的信息", Toast.LENGTH_LONG)
							.show();
				} else if (summinute <= 0 || summinute > 120 || id == 0) {
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "您填写的上课时间不符合要求",
							Toast.LENGTH_LONG).show();
				} else {
					dialog.dismiss();

					// 显示刚添加的内容
					textCourse = textCourses[(id-1 )/ 8][(id-1 ) % 8 ];
					textCourse.setText(course.mytoString());
					new GetcourseColor().addcourse(coursename_sum,
							course.getCoursename());
					new GetcourseColor().initcourse(coursename_sum);
					textCourse.setBackgroundColor(new GetcourseColor()
							.getColor(coursename_sum, course.getCoursename()));

					// int id=new
					// GetcourseId().GetmycourseId(course.getDay(),course.getTimebegin(),
					// course.getTimeend());

					CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
							getActivity());
					SQLiteDatabase coursesDb = courseHelper
							.getWritableDatabase();

					// 查看数据库中该位置的课表是否为空
					String sql1 = "select timebegin from courses  where _id="
							+ id + " limit 1";
					Cursor cursor = coursesDb.rawQuery(sql1, new String[] {});
					cursor.moveToFirst();
					String cString = cursor.getString(cursor
							.getColumnIndex("timebegin"));
					// day,coursename ,timebegin,timeend,cite ,teacher
					// String mydayString = course.getDay() + "";
					// Log.d("TAG","update courses set day='"+mydayString+"' where _id="+id);
					// Log.d("TAG","update courses set coursename='"+course.getCoursename()+"' where _id="+id);
					coursesDb.execSQL("update courses set coursename='"
							+ course.getCoursename() + "' where _id=" + id);
					coursesDb.execSQL("update courses set timebegin='"
							+ course.getTimebegin() + "' where _id=" + id);
					coursesDb.execSQL("update courses set timeend='"
							+ course.getTimeend() + "' where _id=" + id);
					coursesDb.execSQL("update courses set cite='"
							+ course.getCite() + "' where _id=" + id);
					coursesDb.execSQL("update courses set teacher='"
							+ course.getTeacher() + "' where _id=" + id);

					// Log.d("TAG", course+"  "+7);
					// Log.d("TAG", course.getDay()+"  "+8);
					courseHelper.close();
					Log.d("TAG", cString + "   " + 222);
					if (cString.equals("")) {
						Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_LONG)
								.show();
					}

				}

			}
		});

		// 添加一个取消按钮
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
				course.setTeacher(add_teacher.getText().toString());

				int id = new GetcourseId().GetmycourseId(course.getDay(),
						course.getTimebegin(), course.getTimeend());

				if ("".equals(course.getCite()) || "".equals(course.getDay())
						|| "".equals(course.getCoursename())
						|| "".equals(course.getTimebegin())
						|| "".equals(course.getTimeend())
						|| "".equals(course.getTeacher())) {
					// 添加不成功的震动提示
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "内容为空,无法删除",
							Toast.LENGTH_LONG).show();
				} else {
					dialog.dismiss();

					// 删除屏幕上刚删除的内容
					textCourse = textCourses[(id-1 )/ 8][(id-1 )% 8];
					textCourse.setText("");
					textCourse
							.setBackgroundResource(R.drawable.table_course_fangkuang);

					CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
							getActivity());
					SQLiteDatabase coursesDb = courseHelper
							.getWritableDatabase();
					coursesDb
							.execSQL("update courses set coursename='' where _id="
									+ id);
					coursesDb
							.execSQL("update courses set timebegin='' where _id="
									+ id);
					coursesDb
							.execSQL("update courses set timeend='' where _id="
									+ id);
					coursesDb.execSQL("update courses set cite='' where _id="
							+ id);
					coursesDb
							.execSQL("update courses set teacher='' where _id="
									+ id);

					Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
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
		return true;
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
	

}
