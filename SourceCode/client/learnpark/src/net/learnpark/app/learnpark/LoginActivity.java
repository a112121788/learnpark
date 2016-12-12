package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.Setting;
import net.learnpark.app.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆的Activity
 * 
 * @author peng
 * @version 1 2014年6月5日 15:42:30 功能实现 陆礼祥
 */
public class LoginActivity extends ActionBarActivity {
	AutoCompleteTextView username_et;
	EditText password_et;
	Button login_btn;
	ImageView img_password;
	final String[] mailArray = { "@163.com", "@126.com", "@qq.com",
			"@sina.com", "@taobao.com" };
	TextView login_forgetpwd;
	TextView login_register;

	AlertDialog loading_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ActionBar的设置
		Setting.getOverflowMenu(this);
		getSupportActionBar().setTitle("登陆");
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		setContentView(R.layout.activity_login);
		username_et = (AutoCompleteTextView) findViewById(R.id.login_username);
		password_et = (EditText) findViewById(R.id.login_password);
		login_btn = (Button) findViewById(R.id.login_btn);
		login_register = (TextView) findViewById(R.id.login_register);
		login_forgetpwd = (TextView) findViewById(R.id.login_forgetpwd);

		// 密码显示
		img_password = (ImageView) findViewById(R.id.login_photo);
		img_password.setOnClickListener(new OnClickListener() {
			boolean checkpassword = false;

			@Override
			public void onClick(View v) {
				if (!checkpassword) {
					password_et
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
					img_password.setImageDrawable(getResources().getDrawable(
							R.drawable.show_password));
					checkpassword = true;
				} else if (checkpassword) {
					password_et
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
					img_password.setImageDrawable(getResources().getDrawable(
							R.drawable.hide_password));
					checkpassword = false;
				}
			}
		});
		// 邮箱后缀的提示
		final MyAdatper adapter = new MyAdatper(this);
		username_et.setAdapter(adapter);
		username_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String input = s.toString();
				adapter.mList.clear();
				if (input.length() > 0) {
					if (input.indexOf("@") == -1) {
						for (int i = 0; i < mailArray.length; ++i) {
							adapter.mList.add(input + mailArray[i]);
						}
					}
					adapter.notifyDataSetChanged();
					username_et.showDropDown();
				}
			}
		});
		username_et.setThreshold(1);
		// 登陆按钮的监听
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String username = username_et.getText().toString();
				final String password = password_et.getText().toString();
				if ("".equals(username) || password.equals("")) {
					VibratorUtil.Vibrate(LoginActivity.this, 500);
					Toast.makeText(LoginActivity.this, "邮箱、密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (password.length() < 6 || password.length() > 16
						|| !PassWordFormat(password)) {
					VibratorUtil.Vibrate(LoginActivity.this, 500);
					Toast.makeText(LoginActivity.this, "密码格式错误！",
							Toast.LENGTH_SHORT).show();
				} else if (!EmailFormat(username) || username.length() > 31) {
					VibratorUtil.Vibrate(LoginActivity.this, 500);
					Toast.makeText(LoginActivity.this, "邮箱格式错误！",
							Toast.LENGTH_SHORT).show();
				} else {

					AjaxParams params = new AjaxParams();
					try {
						params.put("username",
								URLEncoder.encode(username, "utf-8"));
						params.put("password",
								URLEncoder.encode(password, "utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					FinalHttp fh = new FinalHttp();
					fh.post(NetCantants.CHECK_SERVLET_URL, params,
							new AjaxCallBack<Object>() {
								@Override
								public void onStart() {

									AlertDialog.Builder builder = new Builder(
											LoginActivity.this);
									View layout = getLayoutInflater().inflate(
											R.layout.popup_loading, null);
									builder.setView(layout);
									loading_dialog = builder.create();
									loading_dialog.show();
								};

								public void onLoading(long count, long current) {
								}

								@Override
								public void onSuccess(Object t) {

									if (t != null) {
										if ("1".equals(t)) {

											Toast.makeText(LoginActivity.this,
													"登陆成功", Toast.LENGTH_LONG)
													.show();

											SharedPreferences sp = getSharedPreferences(
													"setting", MODE_PRIVATE);
											SharedPreferences.Editor editor = sp
													.edit();
											editor.putString("username",
													username);
											editor.putString("password",
													password);
											editor.commit();
											startActivity(new Intent(
													getApplicationContext(),
													UserCenterActivity.class));
											finish();
										} else if ("2".equals(t)) {
											Toast.makeText(LoginActivity.this,
													"密码错误", Toast.LENGTH_LONG)
													.show();
										} else if ("0".equals(t)) {
											Toast.makeText(LoginActivity.this,
													"用户不存在", Toast.LENGTH_LONG)
													.show();
										}
									}

									loading_dialog.hide();
								}

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									loading_dialog.hide();
									Toast.makeText(LoginActivity.this, "请打开网络",
											Toast.LENGTH_SHORT).show();
								}
							});
				}
			}

			private boolean PassWordFormat(String password) {
				Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
				Matcher mc = pattern.matcher(password);
				return mc.matches();
			}

			private boolean EmailFormat(String username) {
				// 有问题
				Pattern pattern = Pattern
						.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher mc = pattern.matcher(username);
				return mc.matches();
			}
		});
		// 忘记密码跳转
		login_forgetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ForgetPasswordActivity.class);
				startActivity(intent);
			}
		});
		// 注册新用户跳转
		login_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	private class MyAdatper extends BaseAdapter implements Filterable {

		List<String> mList;
		private Context mContext;
		private MyFilter mFilter;

		public MyAdatper(Context context) {
			mContext = context;
			mList = new ArrayList<String>();
		}

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				TextView tv = new TextView(mContext);
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(20);
				convertView = tv;
			}
			TextView txt = (TextView) convertView;
			txt.setText(mList.get(position));
			return txt;
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new MyFilter();
			}
			return mFilter;
		}

		private class MyFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (mList == null) {
					mList = new ArrayList<String>();
				}
				results.values = mList;
				results.count = mList.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// 这里写重写的方法 一定要retrun true消费这个事件。
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		finish();
	}

}
