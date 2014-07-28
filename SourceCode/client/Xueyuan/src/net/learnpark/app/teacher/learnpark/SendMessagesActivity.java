package net.learnpark.app.teacher.learnpark;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import net.learnpark.app.teacher.learnpark.net.ConnectState;
import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.shixian.QiandaoMessages;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.ByteUtil;
import net.learnpark.app.teacher.learnpark.util.ClassesUtil;
import net.learnpark.app.teacher.learnpark.util.VibratorUtil;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SendMessagesActivity extends ActionBarActivity implements
		OnClickListener {

	private ArrayAdapter<String> selectClassAdapter;
	private Spinner selectClassSpinner;

	private Boolean IsConnected;// 当前网络连接状态
	private String username = "";
	private EditText ed_addclass;
	private EditText ed_addcontent;
	private Button btn_send;
	private String[] data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_sendmessages);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		ed_addclass = (EditText) findViewById(R.id.sendmsg_edittext_classname);
		ed_addcontent = (EditText) findViewById(R.id.sendmsg_editext_content);
		btn_send = (Button) findViewById(R.id.sendmsg_btn_send);

		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"setting", MODE_PRIVATE);
		username = sharedPreferences.getString("user_mail", "");
		// 初始化data集合
		getClasses_data();

		selectClassAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		selectClassAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectClassSpinner = (Spinner) findViewById(R.id.spinner_class);
		selectClassSpinner.setAdapter(selectClassAdapter);
		selectClassSpinner
				.setOnItemSelectedListener(new SpinneronSelectedListener());

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IsConnected = ConnectState
						.isNetworkConnected(SendMessagesActivity.this);
				if (IsConnected == false) {
					Toast.makeText(SendMessagesActivity.this, "当前无网络连接，无法发送",
							Toast.LENGTH_SHORT).show();
				} else if (username.equals("")) {
					VibratorUtil.Vibrate(SendMessagesActivity.this, 200);
					Toast.makeText(SendMessagesActivity.this, "请先登陆帐号",
							Toast.LENGTH_SHORT).show();
				} else if (ed_addclass.getText().toString().equals("")) {
					VibratorUtil.Vibrate(SendMessagesActivity.this, 200);
					Toast.makeText(SendMessagesActivity.this, "请填班级信息",
							Toast.LENGTH_SHORT).show();
				} else if (ed_addcontent.getText().toString().equals("")) {
					VibratorUtil.Vibrate(SendMessagesActivity.this, 200);
					Toast.makeText(SendMessagesActivity.this, "请填写发送信息的内容",
							Toast.LENGTH_SHORT).show();
				} else {
					sendMessages();
				}
			}
		});

	}

	// 获得spinner内的data数组
	private void getClasses_data() {
		List<Classes> classes_list = ClassesUtil
				.getClassesList(SendMessagesActivity.this);
		List<String> data_list = new ArrayList<String>();
		for (Classes classes : classes_list) {
			if (data_list != null && data_list.size() > 0) {
				for (int i = 0; i < data_list.size(); i++) {
					if (classes.getClassname().equals(data_list.get(i))) {
						break;
					} else if (i == data_list.size() - 1) {
						data_list.add(classes.getClassname());
						break;
					}
				}
			} else {
				data_list.add(classes.getClassname());
			}
		}
		data = new String[data_list.size()];
		for (int i = 0; i < data_list.size(); i++) {
			data[i] = data_list.get(i);
		}
	}

	class SpinneronSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// String classnames=ed_addclass.getText().toString();
			// ed_addclass.setText(classnames+"#"+data[position]);
			ed_addclass.setText(data[position]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	// 上传签到信息
	private void sendMessages() {
		// 异步请求
		// 第一个参数：用户接收外接的传入
		// 第二个参数：异步工作进度，更新进度条
		// 第三个参数：子线程返回的值
		AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {
			// 异步工作的进度
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}

			// 这个方法中的代码是在子线程中
			@Override
			protected String doInBackground(String... params) {
				// 发送post请求到服务器
				HttpClient client = new DefaultHttpClient();
				try {
					HttpPost post = new HttpPost(
							NetCantants.SEND_MESSAGES_URL);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
					list.add(new BasicNameValuePair("username", URLEncoder
							.encode(username, "utf-8")));
					list.add(new BasicNameValuePair("classname", URLEncoder
							.encode(ed_addclass.getText().toString(), "utf-8")));
					list.add(new BasicNameValuePair("content",
							URLEncoder.encode(ed_addcontent.getText()
									.toString(), "utf-8")));
					
					// list.add(new BasicNameValuePair("data",data));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
					post.setEntity(entity);// 设置发送的数据
					HttpResponse response = client.execute(post);// 发送请求到服务器
					if (response.getStatusLine().getStatusCode() == 200) {
						byte[] respData = ByteUtil.inputStream2Byte(response);
						// Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
						// data.length);
						return new String(respData);
					} else {
						Log.d("TAG", ""
								+ response.getStatusLine().getStatusCode()
								+ "  data.size()" + 7);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			// 这个方法在主线程中，result就是子线程返回的值
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				// TODO 这里可以更新界面显示的上传进度，暂时没实现
				Log.d("TAG",result+"  result"+88);
				if (result != null && result.equals("true")) {
					Toast.makeText(SendMessagesActivity.this, "发送成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SendMessagesActivity.this, "网络连接异常，请稍候重试",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute(NetCantants.SEND_MESSAGES_URL);
	}

}
