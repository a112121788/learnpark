package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;

import net.learnpark.app.learnpark.entity.Message;
import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发送消息的界面
 * 
 * @author peng
 * 
 */
public class SendMessageActivity extends ActionBarActivity {
	TextView msg_from_tv;
	TextView msg_content_tv;
	EditText msg_receive;
	Button send_btn;
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		String msg_from = getIntent().getStringExtra("msg_from");
		String msg_content = getIntent().getStringExtra("msg_content");
		msg_from_tv = (TextView) findViewById(R.id.msg_from);
		msg_content_tv = (TextView) findViewById(R.id.msg_content);
		msg_receive = (EditText) findViewById(R.id.msg_receive);
		send_btn = (Button) findViewById(R.id.send_btn);
		sp = getSharedPreferences("setting", MODE_PRIVATE);

		msg_from_tv.setText(msg_from);
		msg_content_tv.setText(msg_content);
		msg_content_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (msg_content_tv.getText().toString().contains("http://")
						&& msg_content_tv.getText().toString().endsWith("avi")) {
					Intent intent = new Intent(getApplicationContext(),
							VideoPlayActivity.class);
					intent.putExtra("url", msg_content_tv.getText().toString().trim());
					startActivity(intent);
				}
			}
		});
		send_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (msg_receive.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "请输入回复的内容", 2)
							.show();
					return;
				}
				// 上传数据
				AjaxParams params = new AjaxParams();
				String username = sp.getString("username", "");
				Message message = new Message();
				message.setContent(msg_receive.getText().toString().trim());
				message.setFromUser(username);
				message.setToUser(msg_from_tv.getText().toString());
				message.setTime(new Date());
				String msgGson = new Gson().toJson(message);
				try {
					params.put("username", URLEncoder.encode(username, "utf-8"));
					params.put("msgGson", URLEncoder.encode(msgGson, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				FinalHttp fh = new FinalHttp();
				fh.post(NetCantants.PUT_MESSAGE_URL, params,
						new AjaxCallBack<Object>() {
							@Override
							public void onStart() {
								Toast.makeText(getApplicationContext(), "正在回复",
										1).show();
							};

							public void onLoading(long count, long current) {
							}

							@Override
							public void onSuccess(Object t) {

								if (t != null) {
									if ("true".equals(t)) {

										Toast.makeText(getApplicationContext(),
												"回复成功", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(
												getApplicationContext(),
												MessageActivity.class);
										startActivity(intent);
										finish();
									} else if ("false".equals(t)) {
										Toast.makeText(getApplicationContext(),
												"回复成功,请重试", Toast.LENGTH_SHORT)
												.show();
									}
								}

							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(getApplicationContext(),
										"您的网络未开", Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		startActivity(intent);
		finish();
	}

}