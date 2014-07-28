package net.learnpark.app.teacher.learnpark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.teacher.learnpark.net.ConnectState;
import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 意见反馈界面
 * 
 * @author peng 功能实现by陆礼祥
 */
public class FeedbackActivity extends ActionBarActivity {

	EditText et1;
	AutoCompleteTextView et2;
	final String[] mailArray = { "@163.com", "@126.com", "@qq.com",
			"@sina.com", "@taobao.com" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		getSupportActionBar().setTitle("反馈");
		// 统一ActionBar的背景 by 高帅朋
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		String username = sp.getString("user_mail", "");

		et1 = (EditText) findViewById(R.id.feedback_content);
		et2 = (AutoCompleteTextView) findViewById(R.id.feedback_contact);
		// 邮箱后缀的提示
		et2.setText(username);
		final MyAdatper adapter = new MyAdatper(this);
		et2.setAdapter(adapter);
		et2.addTextChangedListener(new TextWatcher() {

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
					et2.showDropDown();
				}
			}
		});
		et2.setThreshold(1);
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
				tv.setBackgroundResource(R.drawable.auto_table_smallpadding);
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
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.feedback_submit) {
			if (et1.getText().toString().equals("")) {

				Toast.makeText(FeedbackActivity.this, "反馈内容为空，无法反馈",
						Toast.LENGTH_SHORT).show();
			} else if (et2.getText().toString().equals("")) {
				Toast.makeText(FeedbackActivity.this,
						"联系方式为空，我们将无法及时回复您，请填写联系方式", Toast.LENGTH_SHORT).show();
			} else if (!ConnectState.isNetworkConnected(this)) {
				Toast.makeText(FeedbackActivity.this, "没有网络连接，无法反馈",
						Toast.LENGTH_SHORT).show();
			} else {
				Log.d("TAG", "反馈内容 反馈" + "  " + 88);
				String opinion = et1.getText().toString();
				String contact = et2.getText().toString();

				AjaxParams params = new AjaxParams();
				try {
					params.put("yijian", URLEncoder.encode(opinion, "utf-8"));
					params.put("lianxi", URLEncoder.encode(contact, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				FinalHttp fh = new FinalHttp();
				fh.post(NetCantants.SAVE_FEEDBACK_SERVLET_URL, params,
						new AjaxCallBack<Object>() {
							@Override
							public void onLoading(long count, long current) {
								super.onLoading(count, current);

							}

							@Override
							public void onSuccess(Object t) {
								super.onSuccess(t);
								Toast.makeText(FeedbackActivity.this, "谢谢你的反馈",
										Toast.LENGTH_SHORT).show();
								finish();
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(FeedbackActivity.this,
										"网络连接异常，请稍后重试", Toast.LENGTH_SHORT)
										.show();
							}
						});
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
