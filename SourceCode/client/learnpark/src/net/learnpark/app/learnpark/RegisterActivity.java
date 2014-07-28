package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.Setting;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册的Activity
 * 
 * @author peng
 * @version 1 2014年6月5日 15:43:39
 */
public class RegisterActivity extends ActionBarActivity {

	AutoCompleteTextView et1;
	EditText password_et;
	ImageView img_password;
	Button bt;
	CheckBox cb;
	final String[] mailArray = { "@163.com", "@126.com", "@qq.com",
			"@sina.com", "@taobao.com" };
	TextView reqister_agreement_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// ActionBar的设置
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		Setting.getOverflowMenu(this);
		getSupportActionBar().setTitle("注册");
		SysApplication.getInstance().addActivity(this); 

		et1 = (AutoCompleteTextView) findViewById(R.id.register_username);
		password_et = (EditText) findViewById(R.id.register_password);
		bt = (Button) findViewById(R.id.register_btn);
		cb = (CheckBox) findViewById(R.id.register_agreement);
		reqister_agreement_content = (TextView) findViewById(R.id.reqister_agreement_content);

		// 点击协议时跳转到一个网页 传一个网址
		reqister_agreement_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						WebActivity.class);
				startActivity(intent);
			}
		});

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

		bt.setEnabled(false);
		// 邮箱后缀的提示
		final MyAdatper adapter = new MyAdatper(this);
		et1.setAdapter(adapter);
		et1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
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
					et1.showDropDown();
				}
			}
		});
		et1.setThreshold(1);

		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					// bt.setClickable(true);
					bt.setEnabled(true);
				} else {
					// bt.setClickable(false);
					bt.setEnabled(false);
				}

			}
		});

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String username = et1.getText().toString();
				String password = password_et.getText().toString();

				if ("".equals(username) || password.equals("")) {
					Toast.makeText(RegisterActivity.this, "邮箱、密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (password.length() < 6 || password.length() > 16
						|| !PassWordFormat(password)) {
					Toast.makeText(RegisterActivity.this, "密码格式错误！",
							Toast.LENGTH_SHORT).show();
				} else if (!EmailFormat(username) || username.length() > 31) {
					Toast.makeText(RegisterActivity.this, "邮箱格式错误！",
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
					fh.post(NetCantants.REGISTER_SERVLET_URL, params,
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
											Toast.makeText(
													RegisterActivity.this,
													"注册成功", Toast.LENGTH_LONG)
													.show();
										} else if ("false".equals(t)) {
											Toast.makeText(
													RegisterActivity.this,
													"注册失败", Toast.LENGTH_LONG)
													.show();
										} else if ("exist".equals(t)) {
											Toast.makeText(
													RegisterActivity.this,
													"该邮箱已经注册了",
													Toast.LENGTH_LONG).show();
										}
									}
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
				Pattern pattern = Pattern
						.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher mc = pattern.matcher(username);
				return mc.matches();
			}
		});

	}

	class MyAdatper extends BaseAdapter implements Filterable {

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
}