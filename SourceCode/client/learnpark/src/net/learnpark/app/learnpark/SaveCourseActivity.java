package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.learnpark.entity.Course;
import net.learnpark.app.learnpark.entity.GetcourseColor;
import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.FileUtil;
import net.learnpark.app.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveCourseActivity extends ActionBarActivity {
	SharedPreferences sp;
	ImageView up_course;
	ImageView down_course;
	String username;
	Course[][] courses = new Course[7][8];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savecourse);
		// actionBar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("课程备份与导入");
		SysApplication.getInstance().addActivity(this); 
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		up_course = (ImageView) findViewById(R.id.up_course);
		down_course = (ImageView) findViewById(R.id.down_course);

		// 导出到本地

		// 备份
		up_course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = sp.getString("username", "");
				if (username.equals("")) {
					Toast.makeText(getApplicationContext(), "你还未登陆无法进行备份", 2)
							.show();
					return;
				}

				// 查询课程数据
				CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
						getApplicationContext());
				SQLiteDatabase coursesDb = courseHelper.getWritableDatabase();
				String sql = "select * from courses  order by day,timebegin";
				Cursor cursor = coursesDb.rawQuery(sql, new String[] {});
				List<Course> list = new ArrayList<Course>();
				while (cursor.moveToNext()) {

					int id = cursor.getInt(cursor.getColumnIndex("_id"));
					int day = Integer.valueOf(cursor.getString(cursor
							.getColumnIndex("day")));
					int time = cursor.getInt(cursor.getColumnIndex("time"));
					String coursename = cursor.getString(cursor
							.getColumnIndex("coursename"));
					String timebegin = cursor.getString(cursor
							.getColumnIndex("timebegin"));
					String timeend = cursor.getString(cursor
							.getColumnIndex("timeend"));
					String cite = cursor.getString(cursor
							.getColumnIndex("cite"));
					String teacher = cursor.getString(cursor
							.getColumnIndex("teacher"));
					Course course = new Course(id, day, time, coursename,
							timebegin, timeend, cite, teacher);
					list.add(course);
				}
				String coursegson = new Gson().toJson(list);
				// 备份到本地
				// FileUtil.str2File(coursegson,
				// "/mnt/sdcard/learnpark/courses.txt");
				// Toast.makeText(getApplicationContext(),
				// FileUtil.file2Stt("/mnt/sdcard/learnpark/courses.txt"),
				// 2).show();
				// 上传数据
				AjaxParams params = new AjaxParams();
				String username = sp.getString("username", "");
				try {
					params.put("username", URLEncoder.encode(username, "utf-8"));
					params.put("coursegson",
							URLEncoder.encode(coursegson, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				FinalHttp fh = new FinalHttp();
				fh.post(NetCantants.SAVE_COURSE_SERVLET_URL, params,
						new AjaxCallBack<Object>() {
							@Override
							public void onStart() {
								Toast.makeText(SaveCourseActivity.this, "正在备份",
										1).show();
							};

							public void onLoading(long count, long current) {
							}

							@Override
							public void onSuccess(Object t) {

								if (t != null) {
									if ("true".equals(t)) {

										Toast.makeText(SaveCourseActivity.this,
												"备份成功", Toast.LENGTH_LONG)
												.show();

										VibratorUtil.Vibrate(
												SaveCourseActivity.this, 1000);
									} else if ("false".equals(t)) {
										Toast.makeText(SaveCourseActivity.this,
												"备份失败,请重试", Toast.LENGTH_LONG)
												.show();
									}
								}

							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(SaveCourseActivity.this,
										"请打开网络", Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		// 导入
		down_course.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = sp.getString("username", "");
				if (username.equals("")) {
					Toast.makeText(getApplicationContext(), "你还未登陆无法进行导入", 2)
							.show();
					return;
				}

				FinalHttp fh = new FinalHttp();
				AjaxParams params = new AjaxParams();
				try {
					params.put("username", URLEncoder.encode(username, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				fh.post(NetCantants.GET_COURSE_SERVLET_URL, params,
						new AjaxCallBack<Object>() {
							@Override
							public void onStart() {
								Toast.makeText(SaveCourseActivity.this, "正在导入",
										1).show();
							};

							public void onLoading(long count, long current) {
							}

							@Override
							public void onSuccess(Object t) {

								if (t != null) {
									if ("false".equals(t)) {

										Toast.makeText(SaveCourseActivity.this,
												"请先备份", Toast.LENGTH_LONG)
												.show();
										VibratorUtil.Vibrate(
												SaveCourseActivity.this, 1000);
									} else {

										// 删除课程数据
										CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
												getApplicationContext());
										SQLiteDatabase coursesDb = courseHelper
												.getWritableDatabase();
										String sql = "delete from courses";
										Cursor cursor = coursesDb.rawQuery(sql,
												new String[] {});
										// 导入课程数据

										Gson g = new Gson();
										Type typeOfT = new TypeToken<List<Course>>() {
										}.getType();
										List<Course> coursesList = g.fromJson(
												(String) t, typeOfT);
										importCourse(coursesList);

										Toast.makeText(SaveCourseActivity.this,
												"导入成功", Toast.LENGTH_LONG)
												.show();
										VibratorUtil.Vibrate(
												SaveCourseActivity.this, 1000);
									}
								}

							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(SaveCourseActivity.this,
										"请打开网络", Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
	}

	private void importCourse(List<Course> mcourse) {
		if (mcourse != null && mcourse.size() > 0) {
			CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
					SaveCourseActivity.this);
			SQLiteDatabase coursesDb = courseHelper.getWritableDatabase();
			for (Course course : mcourse) {
				if (!course.getTimebegin().equals("")) {
					int id = (course.getDay() - 1) * 8 + course.getTime();
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
				}
			}
			courseHelper.close();
		}
	}
}
