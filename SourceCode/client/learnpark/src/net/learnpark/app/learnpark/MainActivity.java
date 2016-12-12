package net.learnpark.app.learnpark;

import net.learnpark.app.learnpark.fragment.CheckInFragment;
import net.learnpark.app.learnpark.fragment.CourseInfoFragment;
import net.learnpark.app.learnpark.fragment.MoreFragment;
import net.learnpark.app.learnpark.fragment.NetClassFragment;
import net.learnpark.app.learnpark.fragment.ScheduleFragment;
import net.learnpark.app.learnpark.util.Setting;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 程序的入口
 * 
 * @author peng 2014年6月6日 15:31:05
 * @version 2 大致不变了
 */
public class MainActivity extends ActionBarActivity {
	LinearLayout home_page;
	ListView course_info;
	// 底部标签
	ImageView checkin_footer;
	ImageView course_footer;
	ImageView schedule_footer;
	ImageView onlineclass_footer;
	ImageView more_footer;
	FragmentManager fm;
	CheckInFragment checkInFragment;
	CourseInfoFragment courseInfoFragment;
	ScheduleFragment scheduleFragment;
	NetClassFragment onlineclassFragment;
	MoreFragment moreFragment;
	FragmentTransaction ft;
	final static int requestCode = 1;
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fm = getSupportFragmentManager();
		// ActionBar的设置
		Setting.getOverflowMenu(this);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this);
		SysApplication.getInstance().addActivity(this); 
		// 推送的实现
		// JPushInterface.setDebugMode(true);
		// JPushInterface.init(this);
		// 友盟更新
		// UmengUpdateAgent.update(this);

		home_page = (LinearLayout) findViewById(R.id.home_page);

		// 判断用户是第一次登陆
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		editor = sp.edit();
		Boolean isFirst = sp.getBoolean("isFirst", true);// 如果为设置 就去为false
		if (isFirst) {
			// 第一次打开app 创建课程表数据库
			CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
					MainActivity.this);
			SQLiteDatabase coursesDb = courseHelper.getWritableDatabase();
			courseHelper.close();
			// 把isFirst设置为false
			editor = sp.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
			startActivity(new Intent(getApplicationContext(),
					FirstInActivity.class));
		}

		// 底部标签的实现
		checkin_footer = (ImageView) findViewById(R.id.checkin_footer);
		course_footer = (ImageView) findViewById(R.id.course_footer);
		schedule_footer = (ImageView) findViewById(R.id.schedule_footer);
		onlineclass_footer = (ImageView) findViewById(R.id.onlineclass_footer);
		more_footer = (ImageView) findViewById(R.id.more_footer);

		// 底部标签的监听实现
		OnFooterTabClickListener footerTabClickListener = new OnFooterTabClickListener();
		checkin_footer.setOnClickListener(footerTabClickListener);
		course_footer.setOnClickListener(footerTabClickListener);
		schedule_footer.setOnClickListener(footerTabClickListener);
		onlineclass_footer.setOnClickListener(footerTabClickListener);
		more_footer.setOnClickListener(footerTabClickListener);

		// 初始底部数据
		course_footer.setBackgroundColor(0XFF76EEC6);
		checkin_footer.setBackgroundColor(Color.WHITE);
		schedule_footer.setBackgroundColor(Color.WHITE);
		onlineclass_footer.setBackgroundColor(Color.WHITE);
		more_footer.setBackgroundColor(Color.WHITE);
		// 最后换成替换
		ft = fm.beginTransaction();
		ft.replace(R.id.content, new CourseInfoFragment());
		ft.commit();

		// fragment
		checkInFragment = new CheckInFragment();
		courseInfoFragment = new CourseInfoFragment();
		onlineclassFragment = new NetClassFragment();
		scheduleFragment = new ScheduleFragment();
		moreFragment = new MoreFragment();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.user_center) {
			// 跳转到个人中心 如果用户未登陆跳转到登陆界面
			final String username = sp.getString("username", "");
			final String password = sp.getString("password", "");
			if ("".equals(username)) {
				Toast.makeText(getApplicationContext(), "你还没有登录，请登陆", 2).show();
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			} else {
				startActivity(new Intent(MainActivity.this,
						UserCenterActivity.class));
			}
		}
		if (id == R.id.course_share) {
			// 跳转到课程共享界面
			// CaptureActivity.class 二维码
			Intent intent = new Intent(MainActivity.this,
					CourseShareActivity.class);
			startActivity(intent);
		}

		if (id == R.id.feedback) {
			// 跳转到意见界面
			startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
		}
		if (id == R.id.teacherinfo) {
			// 跳转到我的老师
			startActivity(new Intent(MainActivity.this,
					TeacherInfoActivity.class));
		}

		if (id == R.id.howtouse) {
			// 使用帮助
			startActivity(new Intent(MainActivity.this, FirstInActivity.class));
		}
		// if (id == R.id.updata) {
		// // 软件更新
		// UpdateManage updatem = new UpdateManage(this);
		// updatem.checkUpdateInfo();
		// }
		if (id == R.id.about) {
			// 关于
			startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 底部按钮的监听器
	 * 
	 * @author peng
	 * 
	 */
	private class OnFooterTabClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int footerId = v.getId();
			switch (footerId) {
			case R.id.checkin_footer:
				checkin_footer.setBackgroundColor(0XFF76EEC6);
				course_footer.setBackgroundColor(Color.WHITE);
				schedule_footer.setBackgroundColor(Color.WHITE);
				onlineclass_footer.setBackgroundColor(Color.WHITE);
				more_footer.setBackgroundColor(Color.WHITE);
				// 最后换成替换
				ft = fm.beginTransaction();
				if (checkInFragment == null) {
					checkInFragment = new CheckInFragment();
				}
				ft.replace(R.id.content, checkInFragment);
				ft.commit();
				break;
			case R.id.course_footer:
				course_footer.setBackgroundColor(0XFF76EEC6);
				checkin_footer.setBackgroundColor(Color.WHITE);
				schedule_footer.setBackgroundColor(Color.WHITE);
				onlineclass_footer.setBackgroundColor(Color.WHITE);
				more_footer.setBackgroundColor(Color.WHITE);
				// 最后换成替换
				ft = fm.beginTransaction();
				if (courseInfoFragment == null) {
					courseInfoFragment = new CourseInfoFragment();
				}
				ft.replace(R.id.content, courseInfoFragment);
				ft.commit();
				break;
			case R.id.schedule_footer:
				schedule_footer.setBackgroundColor(0XFF76EEC6);
				checkin_footer.setBackgroundColor(Color.WHITE);

				course_footer.setBackgroundColor(Color.WHITE);
				onlineclass_footer.setBackgroundColor(Color.WHITE);
				more_footer.setBackgroundColor(Color.WHITE);

				// 最后换成替换
				ft = fm.beginTransaction();
				if (scheduleFragment == null) {
					scheduleFragment = new ScheduleFragment();
				}
				ft.replace(R.id.content, scheduleFragment);
				ft.commit();
				break;
			case R.id.onlineclass_footer:
				onlineclass_footer.setBackgroundColor(0XFF76EEC6);
				checkin_footer.setBackgroundColor(Color.WHITE);
				schedule_footer.setBackgroundColor(Color.WHITE);
				course_footer.setBackgroundColor(Color.WHITE);
				more_footer.setBackgroundColor(Color.WHITE);
				// 最后换成替换
				ft = fm.beginTransaction();
				if (onlineclassFragment == null) {
					onlineclassFragment = new NetClassFragment();
				}
				ft.replace(R.id.content, onlineclassFragment);
				ft.commit();
				break;
			case R.id.more_footer:
				onlineclass_footer.setBackgroundColor(Color.WHITE);
				checkin_footer.setBackgroundColor(Color.WHITE);
				schedule_footer.setBackgroundColor(Color.WHITE);
				course_footer.setBackgroundColor(Color.WHITE);
				more_footer.setBackgroundColor(0XFF76EEC6);
				// 最后换成替换
				ft = fm.beginTransaction();
				if (moreFragment == null) {
					moreFragment = new MoreFragment();
				}
				ft.replace(R.id.content, moreFragment);
				ft.commit();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("退出学苑？");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SysApplication.getInstance().exit();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
}
