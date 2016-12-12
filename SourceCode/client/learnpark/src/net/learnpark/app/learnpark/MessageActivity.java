package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.learnpark.app.learnpark.entity.Message;
import net.learnpark.app.learnpark.net.NetCantants;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 消息盒子
 * 
 * @author peng
 * 
 */
public class MessageActivity extends ActionBarActivity {
	TabHost tabHost;

	ListView msg_receive_lv;
	ListView msg_send_lv;
	FinalDb msgDb;
	List<Message> list_message;
	LinkedList<Map<String, String>> data;
	LinkedList<Map<String, String>> data_send;
	SimpleAdapter sAdapter;
	SimpleAdapter msg_sendAdapter;
	SharedPreferences sp;
	String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		// 收件箱
		tabHost = (TabHost) findViewById(R.id.all_columns);
		tabHost.setup();
		TabSpec my_tab1 = tabHost.newTabSpec("msg_receive").setIndicator("收件箱")
				.setContent(R.id.msg_receive);
		// 发件箱
		tabHost.addTab(my_tab1);
		TabSpec my_tab2 = tabHost.newTabSpec("msg_send").setIndicator("发件箱")
				.setContent(R.id.msg_send);
		tabHost.addTab(my_tab2);

		sp = getSharedPreferences("setting", MODE_PRIVATE);

		msg_receive_lv = (ListView) findViewById(R.id.msg_receive);
		msg_send_lv = (ListView) findViewById(R.id.msg_send);

		// 准备收件箱的数据
		data = new LinkedList<Map<String, String>>();
		int[] to = { R.id.msg_from, R.id.msg_content, R.id.msg_time };
		String[] from = { "msg_from", "msg_content", "msg_time" };
		sAdapter = new SimpleAdapter(getApplicationContext(), data,
				R.layout.item_msg_receive, from, to);
		msg_receive_lv.setAdapter(sAdapter);

		// 准备收件箱的数据 网路上的
		username = sp.getString("username", "");
		if (username.equals("")) {
			Toast.makeText(getApplicationContext(), "你还未登陆无法查看最新消息", 2).show();
			finish();
			return;
		}
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();
		try {
			params.put("username", URLEncoder.encode(username, "utf-8"));
			params.put("fromuser", "false");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		fh.post(NetCantants.GET_MESSAGE_URL, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {

						if (t != null) {
							if ("false".equals(t)) {

								Toast.makeText(getApplicationContext(), "无数据",
										Toast.LENGTH_LONG).show();
							} else {

								// 查看最新消息
								Gson g = new Gson();
								Type typeOfT = new TypeToken<List<Message>>() {
								}.getType();
								List<Message> msgList = g.fromJson((String) t,
										typeOfT);
								if (msgList != null) {
									for (int i = 0; i < msgList.size(); i++) {
										Map<String, String> map = new ArrayMap<String, String>();
										map.put("msg_from", msgList.get(i)
												.getFromUser());
										map.put("msg_content", "  "
												+ msgList.get(i).getContent());
										map.put("msg_time", getTimeStrs(msgList
												.get(i).getTime().getTime()));
										if (!data.contains(map)) {
											data.addFirst(map);
										}
										sAdapter.notifyDataSetChanged();
									}
								}
							}
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(getApplicationContext(), "请打开网络",
								Toast.LENGTH_SHORT).show();
					}
				});

		// 准备发件箱的数据 网路上的
		int[] to1 = { R.id.msg_to, R.id.msg_content, R.id.msg_time };
		String[] from1 = { "msg_to", "msg_content", "msg_time" };
		data_send = new LinkedList<Map<String, String>>();
		msg_sendAdapter = new SimpleAdapter(getApplicationContext(), data_send,
				R.layout.item_msg_send, from1, to1);
		msg_send_lv.setAdapter(msg_sendAdapter);

		AjaxParams params1 = new AjaxParams();
		try {
			params1.put("username", URLEncoder.encode(username, "utf-8"));
			params1.put("fromuser", "true");// 标记 代表是获取发件信息

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		FinalHttp fh1 = new FinalHttp();
		fh1.post(NetCantants.GET_MESSAGE_URL, params1,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {

						if (t != null) {
							if ("false".equals(t)) {

								Toast.makeText(getApplicationContext(), "无数据",
										Toast.LENGTH_LONG).show();
							} else {

								// 查看最新消息
								Gson g = new Gson();
								Type typeOfT = new TypeToken<List<Message>>() {
								}.getType();
								List<Message> msgList = g.fromJson((String) t,
										typeOfT);
								if (msgList != null) {
									for (int i = 0; i < msgList.size(); i++) {
										Map<String, String> map = new ArrayMap<String, String>();
										map.put("msg_to", msgList.get(i)
												.getToUser());
										map.put("msg_content", "  "
												+ msgList.get(i).getContent());
										map.put("msg_time", getTimeStrs(msgList
												.get(i).getTime().getTime()));
										if (!data_send.contains(map)) {
											data_send.addFirst(map);
										}
										msg_sendAdapter.notifyDataSetChanged();
									}
								}
							}
						}

					}
				});

		// 每条消息的监听函数
		msg_receive_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String msg_content = data.get(position).get("msg_content")
						.trim();
				if (msg_content.contains("http://")
						&& msg_content.endsWith("avi")) {
					Intent intent = new Intent(getApplicationContext(),
							VideoPlayActivity.class);
					intent.putExtra(
							"url",
							msg_content.substring(
									msg_content.indexOf("http://"),
									msg_content.length()));
					startActivity(intent);
				} else {

					Intent intent = new Intent(getApplicationContext(),
							SendMessageActivity.class);
					intent.putExtra("msg_from",
							data.get(position).get("msg_from"));
					intent.putExtra("msg_content",
							data.get(position).get("msg_content"));
					startActivity(intent);
					finish();
				}
			}
		});

		// 准备发件箱的数据

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private String getTimeStrs(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		long msgtime = calendar.getTimeInMillis();
		long currenttime = System.currentTimeMillis();
		long timeDiata = currenttime - msgtime;

		if (timeDiata > 24 * 60 * 60 * 1000) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String time = (month + 1) + "月" + day + "日";
			return time;
		} else {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			String time = (hour > 9 ? hour : "0" + hour) + ":"
					+ (minute > 9 ? minute : "0" + minute) + ":"
					+ (second > 9 ? second : "0" + second);
			return time;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.msg_fresh) {
			FinalHttp fh = new FinalHttp();
			AjaxParams params = new AjaxParams();
			try {
				params.put("username", URLEncoder.encode(username, "utf-8"));
				params.put("fromuser", "false");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fh.post(NetCantants.GET_MESSAGE_URL, params,
					new AjaxCallBack<Object>() {
						@Override
						public void onStart() {
							Toast.makeText(getApplicationContext(), "正在刷新", 1)
									.show();
						}

						@Override
						public void onSuccess(Object t) {

							if (t != null) {
								if ("false".equals(t)) {

									Toast.makeText(getApplicationContext(),
											"无新消息", 1).show();
								} else {
									int count = 0;
									// 查看最新消息
									Gson g = new Gson();
									Type typeOfT = new TypeToken<List<Message>>() {
									}.getType();
									List<Message> msgList = g.fromJson(
											(String) t, typeOfT);
									if (msgList != null) {
										for (int i = 0; i < msgList.size(); i++) {
											Map<String, String> map = new ArrayMap<String, String>();
											map.put("msg_from", msgList.get(i)
													.getFromUser());
											map.put("msg_content", "  "
													+ msgList.get(i)
															.getContent());
											map.put("msg_time",
													getTimeStrs(msgList.get(i)
															.getTime()
															.getTime()));
											if (!data.contains(map)) {
												data.addFirst(map);
												count++;
											}
											sAdapter.notifyDataSetChanged();

										}
										Toast.makeText(getApplicationContext(),
												count + "条新消息", 1).show();
									}
								}
							}

						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(), "请打开网络",
									Toast.LENGTH_SHORT).show();
						}
					});
		}
		return true;
	}
}