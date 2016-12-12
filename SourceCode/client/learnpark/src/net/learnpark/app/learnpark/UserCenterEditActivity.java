package net.learnpark.app.learnpark;

import net.learnpark.app.learnpark.net.NetCantants;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户个人中心 修改
 * 
 * @author peng
 * @version 1 2014-6-12 14:12:46
 */
public class UserCenterEditActivity extends ActionBarActivity {

	TextView user_name;
	TextView user_mail;
	TextView user_school;
	TextView user_number;
	TextView user_grade;
	TextView user_major;
	TextView user_userclass;
	TextView tv5;
	String photoname = null;
	String timename = null;// 头像的名称
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center_edit);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		
		user_name = (TextView) findViewById(R.id.user_name);
		user_mail = (TextView) findViewById(R.id.user_mail);
		user_school = (TextView) findViewById(R.id.user_school);
		user_number = (TextView) findViewById(R.id.user_number);
		user_grade = (TextView) findViewById(R.id.user_grade);
		user_major = (TextView) findViewById(R.id.user_major);
		user_userclass = (TextView) findViewById(R.id.user_userclass);

		sp = getSharedPreferences("setting", MODE_PRIVATE);
		user_name.setText(sp.getString("name", ""));
		user_mail.setText(sp.getString("username", ""));
		user_number.setText(sp.getString("user_number", ""));
		user_school.setText(sp.getString("user_school", ""));
		user_grade.setText(sp.getString("user_grade", ""));
		user_major.setText(sp.getString("user_major", ""));
		user_userclass.setText(sp.getString("user_userclass", ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.usercenter_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.usercenter_submit) {
			// 上传用户更新的信息
			AjaxParams params = new AjaxParams();
			params.put("username", user_mail.getText().toString());
			params.put("name", user_name.getText().toString());
			params.put("user_number", user_number.getText().toString());
			params.put("user_school", user_school.getText().toString());
			params.put("user_grade", user_grade.getText().toString());
			params.put("user_major", user_major.getText().toString());
			params.put("user_userclass", user_userclass.getText().toString());
			FinalHttp fh = new FinalHttp();
			fh.post(NetCantants.MODIFY_USER_INFO_URL, params,
					new AjaxCallBack<Object>() {
						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
						}
						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);
							if (t != null) {
								if ("true".equals(t)) {
									Toast.makeText(getApplicationContext(),
											"修改成功", 1).show();
									startActivity(new Intent(
											getApplicationContext(),
											UserCenterActivity.class));
									finish();
								} else if ("false".equals(t)) {
									Toast.makeText(getApplicationContext(),
											"修改失败", 1).show();
									startActivity(new Intent(
											getApplicationContext(),
											UserCenterActivity.class));
									finish();
								}
							}
						}
					});
		}
		return true;
	}
}
