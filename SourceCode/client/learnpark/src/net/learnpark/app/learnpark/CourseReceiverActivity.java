package net.learnpark.app.learnpark;

import java.lang.reflect.Type;
import java.util.List;

import net.learnpark.app.learnpark.entity.Course;
import net.learnpark.app.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 导入课程的Activity 修改功能，改为课程共享
 * 
 * @author peng
 * @version 1 2014年6月5日 15:41:55
 */

public class CourseReceiverActivity extends ActionBarActivity {
	Button share_course_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses_receiver);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		share_course_ok = (Button) findViewById(R.id.share_course_ok);
		share_course_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				finish();
			}
		});

		String url = getIntent().getStringExtra("url");

		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();
		fh.get(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {

				if (t != null) {

					// 删除课程数据
					CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
							getApplicationContext());
					SQLiteDatabase coursesDb = courseHelper
							.getWritableDatabase();
					String sql = "delete from courses";
					Cursor cursor = coursesDb.rawQuery(sql, new String[] {});
					// 导入课程数据

					Gson g = new Gson();
					Type typeOfT = new TypeToken<List<Course>>() {
					}.getType();
					List<Course> coursesList = g.fromJson((String) t, typeOfT);
					importCourse(coursesList);

					Toast.makeText(CourseReceiverActivity.this, "导入成功", 1)
							.show();
					VibratorUtil.Vibrate(CourseReceiverActivity.this, 1000);
				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				Toast.makeText(CourseReceiverActivity.this,
						"导入失败，请查看两个手机时候在同一个局域网", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	private void importCourse(List<Course> mcourse) {
		if (mcourse != null && mcourse.size() > 0) {
			CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
					CourseReceiverActivity.this);
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