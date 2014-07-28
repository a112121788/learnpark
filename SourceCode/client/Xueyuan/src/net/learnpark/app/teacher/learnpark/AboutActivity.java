package net.learnpark.app.teacher.learnpark;

import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.Setting;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * 关于界面
 * 
 * @author 张晓午
 * 
 */
public class AboutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_about_us);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		TextView tv_nownum = (TextView) findViewById(R.id.version_nownum);
		tv_nownum.setText(Setting.getVersionName(this));

	}
}
