package net.learnpark.app.teacher.learnpark;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.Setting;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 忘记密码找回的Activity
 * 
 * @author peng
 * @version 1 2014年6月11日 15:46:36
 */
public class ForgetPasswordActivity extends ActionBarActivity {

	AutoCompleteTextView et;
	Button bt;
	final String[] mailArray = { "@163.com", "@126.com", "@qq.com",
			"@sina.com", "@taobao.com" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		// ActionBar的设置
		Setting.getOverflowMenu(this);
		getSupportActionBar().setTitle("忘记密码");
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		et = (AutoCompleteTextView) findViewById(R.id.forgetpassword_email);
		bt = (Button) findViewById(R.id.id_forgetpassword_btn);

		// 邮箱后缀的提示
		final MyAdatper adapter = new MyAdatper(this);
		et.setAdapter(adapter);
		et.addTextChangedListener(new TextWatcher() {

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
					et.showDropDown();
				}
			}
		});
		et.setThreshold(1);

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mail = et.getText().toString();

				if ("".equals(mail)) {
					Toast.makeText(ForgetPasswordActivity.this, "邮箱不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (!EmailFormat(mail) || mail.length() > 31) {
					Toast.makeText(ForgetPasswordActivity.this, "邮箱格式错误！",
							Toast.LENGTH_SHORT).show();
				} else {
					AjaxParams params = new AjaxParams();
					params.put("mail", mail);
					FinalHttp fh = new FinalHttp();
					fh.post(NetCantants.FORGET_PASSWORD_SERVLET_URL, params,
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
													ForgetPasswordActivity.this,
													"邮件发送成功，注意查看！",
													Toast.LENGTH_SHORT).show();
										} else if ("false".equals(t)) {
											Toast.makeText(
													ForgetPasswordActivity.this,
													"用户不存在，请重新输入！",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
							});
				}
			}

			private boolean EmailFormat(String mail) {
				Pattern pattern = Pattern
						.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher mc = pattern.matcher(mail);
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
