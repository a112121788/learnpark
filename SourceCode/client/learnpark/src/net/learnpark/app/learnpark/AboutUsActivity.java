package net.learnpark.app.learnpark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * 关于界面。
 * 
 * @author 胡亚历
 */
public class AboutUsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("关于学苑");
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
