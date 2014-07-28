package net.learnpark.app.teacher.learnpark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.learnpark.app.teacher.learnpark.shixian.QiandaoMessages;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.StudentsUtil;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DianmingActivity extends ActionBarActivity {

	private final int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
	@SuppressWarnings("deprecation")
	private final int fp = ViewGroup.LayoutParams.FILL_PARENT;
	Button bt_start;
	TableLayout tableLayout;
	TextView tv;

	private String coursename;// 专业名称
	private String classname; // 班级
	private int StudentsSum; // 班级人数
	private List<Students> stu;

	private WifiManager mWifiManager;
	private String SSID;
	private String PASSWORD;
	private static int Port = 55555;
	private Thread dianming;
	private List<Socket> mList = new ArrayList<Socket>();
	private ExecutorService mExecutorService = null; // thread pool
	private ServerSocket server = null;
	private int tabtnum = 0;
	private String date;
	// 用于存储现在tab的状态
	private Boolean[] state = null;

	private Boolean isDianmingIng = false;
	// 设置hander的值
	private int None = 1;

	// wifi当前的状态
	public static final int WIFI_AP_STATE_DISABLING = 0;
	public static final int WIFI_AP_STATE_DISABLED = 1;
	public static final int WIFI_AP_STATE_ENABLING = 2;
	public static final int WIFI_AP_STATE_ENABLED = 3;
	public static final int WIFI_AP_STATE_FAILED = 4;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// Log.d("TAG", Num + "  " + "handler");
			tv = (TextView) tableLayout.findViewById(msg.what);
			// Log.d("TAG", tv.getText() + "  " + "handler,gettext");
			tv.setBackgroundResource(R.drawable.table_shape_text2);
			// Log.d("TAG", Num + "  " + "handler");

		};
	};

	@SuppressLint("HandlerLeak")
	private Handler handler_no = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// Log.d("TAG", Num + "  " + "handler");
			tv = (TextView) tableLayout.findViewById(msg.what);
			// Log.d("TAG", tv.getText() + "  " + "handler,gettext");
			tv.setBackgroundResource(R.drawable.table_shape_text1);
			// Log.d("TAG", Num + "  " + "handler");

		};
	};

	@SuppressLint("HandlerLeak")
	private Handler handler_toast = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == None) {
				Toast.makeText(DianmingActivity.this, None + "没有该学生信息，签到失败",
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_dianming);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		// 获得传过来的课程信息
		Intent intent = getIntent();
		classname = intent.getStringExtra("classname");
		coursename = intent.getStringExtra("coursename");

		// 获得老师的用户名，以此来创建热点，来进行点名
		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		String usermail = sp.getString("user_mail", "");
		String sss = usermail.substring(0, usermail.lastIndexOf("@"));
		SSID = "lp" + sss;
		PASSWORD = "a" + sss + "a";
		Log.d("TAG", "SSID   " + SSID + "  " + 5588);
		Log.d("TAG", "PASSWORD   " + PASSWORD + "  " + 5588);

		// 获得当前系统的时间，存入数据库
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		date = year + "-" + month + "-" + day;

		stu = new ArrayList<Students>();
		// 获得所有学生的列表
		stu = StudentsUtil.getStudentsList(this, classname);
		StudentsSum = stu.size();
		state = new Boolean[StudentsSum];

		// 获取wifi管理服务
		mWifiManager = (WifiManager) DianmingActivity.this
				.getSystemService(Context.WIFI_SERVICE);

		// 新建表格的实例
		tableLayout = (TableLayout) findViewById(R.id.dianming_table);
		// 全部列自动填充空白处
		tableLayout.setStretchAllColumns(true);
		// 生成m行，n列的表格
		int m = StudentsSum / 7 + 1, n = 7;
		if (StudentsSum != 0) {
			for (int row = 0; row < m; row++) {
				TableRow tableRow = new TableRow(DianmingActivity.this);
				for (int col = 1; col <= n; col++) {
					tabtnum += 1;
					if (tabtnum > StudentsSum) {
						break;
					}
					state[tabtnum - 1] = false;
					TextView tv = new TextView(DianmingActivity.this);
					tv.setBackgroundResource(R.drawable.table_shape_text1);
					Log.d("TAG", row * 7 + col + "    Id");
					tv.setId(tabtnum);
					tv.setGravity(Gravity.CENTER);
					tv.setText(tabtnum + "");
					tableRow.addView(tv);

					// 这里给每一名学生设置点击事件，这样老师可以手动选择签到的学生名单
					tv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int id = Integer.valueOf(v.getId());
							PutInStudent(id);
						}
					});

				}
				// 新建的TableRow添加到TableLayout
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(fp,
						wc));
			}
		}

		bt_start = (Button) findViewById(R.id.dianming_button_start);
		Button bt_finish = (Button) findViewById(R.id.tab1_button_jieshu);

		// 设置按钮监听事件
		bt_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 显示现在的状态是正在点名
				if (StudentsSum != 0) {
					ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
					pb.setVisibility(View.VISIBLE);
					bt_start.setText("正在点名");
					bt_start.setBackgroundResource(R.drawable.table_shape_button_nostroke);

					// 这里要判断一下wifi热点是否开启，如果开启就关闭它
					int wifiap = getWifiApState() % 10;
					Log.d("TAG", "wifiap" + wifiap + "  " + 88888);
					if (wifiap == 2 || wifiap == 3) {
						// Log.d("TAG", "wifiap"+wifiap + "  " + 5588);
						closeAp();
					}
					// 打开wifi
					// setWifiApEnabled(true);
					OpenWifiAp(true);
					isDianmingIng = true;
					dianming = new Thread() {
						@Override
						public void run() {
							super.run();
							try {

								Log.d("TAG", "开始监听端口了"+ "  " + 5588);
								server = new ServerSocket(Port);
								// create a thread pool
								mExecutorService = Executors
										.newCachedThreadPool();
								// System.out.print("server start ...");
								Socket client = null;
								while (true) {
									client = server.accept();
									mList.add(client);
									Log.d("TAG", "新建了一个线程" + "  " + 5588);
									// start a new thread to handle the
									// connection
									mExecutorService
											.execute(new Service(client));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					};

					dianming.start();
				} else {
					Toast.makeText(DianmingActivity.this, "您选择的班级没有学生，无法点名",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		bt_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 停止点名
				if (!bt_start.getText().toString().equals("开始点名")) {

					ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
					pb.setVisibility(View.INVISIBLE);
					bt_start.setText("重新点名");
					bt_start.setBackgroundResource(R.drawable.table_shape_button_nostroke);

					stopDianming();
				} else {
					Toast.makeText(DianmingActivity.this, "您尚未开始点名，无法结束点名",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// super.onCreateOptionsMenu(menu);
		// MenuItem mnu1 = menu.add(0, 0, 0, "统计结果");
		// {
		// MenuItemCompat.setShowAsAction(mnu1,
		// MenuItemCompat.SHOW_AS_ACTION_ALWAYS
		// | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		// }
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if (item.getItemId() == 0) {
		// Intent intent = new Intent(this, DianmingResultActivity.class);
		// startActivity(intent);
		// }
		switch (item.getItemId()) {
		case android.R.id.home:
			if (isDianmingIng == true) {

				AlertDialog alertDialog;
				AlertDialog.Builder bulider = new Builder(DianmingActivity.this);
				bulider.setIcon(R.drawable.ic_launcher);

				View layout = (View) LayoutInflater.from(DianmingActivity.this)
						.inflate(R.layout.dainming_back, null);
				bulider.setView(layout);
				// 添加一个确定添加按钮
				bulider.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								stopDianming();
								dialog.dismiss();
								finish();
							}
						});

				// 添加一个取消按钮
				bulider.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				alertDialog = bulider.create();
				alertDialog.show();

			} else {
				finish();
			}

		}
		return super.onOptionsItemSelected(item);
	}

	// 监听返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isDianmingIng == true) {

				AlertDialog alertDialog;
				AlertDialog.Builder bulider = new Builder(DianmingActivity.this);
				bulider.setIcon(R.drawable.ic_launcher);

				View layout = (View) LayoutInflater.from(DianmingActivity.this)
						.inflate(R.layout.dainming_back, null);
				bulider.setView(layout);
				// 添加一个确定添加按钮
				bulider.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								stopDianming();
								dialog.dismiss();
								finish();
							}
						});

				// 添加一个取消按钮
				bulider.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				alertDialog = bulider.create();
				alertDialog.show();

			} else {
				finish();
			}

		}
		return false;
	}

	// 开启wifi
	public Boolean OpenWifiAp(boolean enabled) {
		if (enabled) { // disable WiFi in any case
			// wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
			mWifiManager.setWifiEnabled(false);
		}
		boolean isHtc = false;
		try {
			isHtc = WifiConfiguration.class.getDeclaredField("mWifiApProfile") != null;
		} catch (java.lang.NoSuchFieldException e) {
			isHtc = false;
		}

		WifiConfiguration apConfig = new WifiConfiguration();
		apConfig.SSID = SSID;
		apConfig.preSharedKey = PASSWORD;
		apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

		if (isHtc) {
			setHTCSSID(apConfig);
		}

		// 通过反射调用设置热点
		Method method;
		try {
			method = mWifiManager.getClass().getMethod("setWifiApEnabled",
					WifiConfiguration.class, Boolean.TYPE);

			// 返回热点打开状态
			return (Boolean) method.invoke(mWifiManager, apConfig, enabled);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// wifi热点开关
	public boolean setWifiApEnabled(boolean enabled) {
		if (enabled) { // disable WiFi in any case
			// wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
			mWifiManager.setWifiEnabled(false);
		}
		try {
			// 热点的配置类
			WifiConfiguration apConfig = new WifiConfiguration();
			// 配置热点的名称(可以在名字后面加点随机数什么的)
			apConfig.SSID = SSID;
			// 配置热点的密码
			apConfig.preSharedKey = PASSWORD;
			// 通过反射调用设置热点

			apConfig.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			apConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);
			apConfig.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			apConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			apConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			apConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			apConfig.status = WifiConfiguration.Status.ENABLED;
			apConfig.wepTxKeyIndex = 0;

			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			// 返回热点打开状态
			method.invoke(mWifiManager, apConfig);
			return (Boolean) method.invoke(mWifiManager, apConfig, enabled);
		} catch (Exception e) {
			return false;
		}
	}

	// 创建一个线程在后台监听
	class Service implements Runnable {
		private Socket socket;
		private BufferedReader in = null;
		private String thismsg = "";
		private int Num;

		public Service(Socket socket) {
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				// msg = "user" + this.socket.getInetAddress() + "come toal:"+
				// mList.size();
				thismsg = in.readLine();
				// in.close();
				String[] allmsg = thismsg.split("#");
				// String thisname = messages[0];
				String thisnum = allmsg[1];

				Log.d("TAG", thisnum + "  获取了他的信息" + "     " + 5588);

				// for (int i = 0; i < stu.size(); i++) {
				// Log.d("TAG", stu.get(i).getStunum() + "     " + 5588);
				// }

				for (int i = 0; i < stu.size(); i++) {
					if (thisnum.equals(stu.get(i).getStudentnum())) {
						stu.get(i).setRemark("yes");
						stu.get(i).setImei(allmsg[0]);

						// 下面更改签到显示页面
						Num = Integer.valueOf(thisnum.substring(thisnum
								.length() - 3));
						Log.d("TAG", thismsg + "  " + 4444488);
						Message msg = new Message();
						// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
						msg.what = Num;
						handler.sendMessage(msg);
						this.sendmsg();
						break;
					}/*
					 * else if(i==stu.size()-1){ Message msg = new Message();
					 * //Log.d("TAG", thisname + "     " + Num + "  " + 5588);
					 * msg.what = Num; handler_toast.sendMessage(msg); break; }
					 */
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
		}

		public void sendmsg() {
			// System.out.println(msg);
			// int num = mList.size();

			PrintWriter pout = null;
			try {
				pout = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				pout.println("I have got your message\n");
				Log.d("TAG", "签到ok55555555" + "  " + 5588);
				// System.out.println("ssssssss");
				in.close();
				pout.close();
				// msg = "user:" + socket.getInetAddress()+
				// "exit total:" + mList.size();
				socket.close();
				mList.remove(socket);
				Log.d("TAG", "结束了一个线程" + "  " + 5588);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					if (socket != null) {
						socket.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// 得到wifi热点当前的状态
	public int getWifiApState() {
		try {
			Method method = mWifiManager.getClass().getMethod("getWifiApState");
			return (Integer) method.invoke(mWifiManager);
		} catch (Exception e) {
			e.printStackTrace();
			return WIFI_AP_STATE_FAILED;
		}
	}

	// 关闭wifi热点
	public boolean closeAp() {
		try {
			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
			return (Boolean) method.invoke(mWifiManager, null, false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 停止点名，并保存点名信息
	public void stopDianming() {
		closeAp();

		try {
			if (server != null) {
				server.close();
				server = null;
			}
			for (Socket server : mList) {
				if (server != null) {
					server.close();
					server = null;
					mList.remove(server);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("TAG", "结束了所有线程" + "  " + 5588);

		// 点击签到结束后将签到信息存入数据库内
		FinalDb qiandaoDb = FinalDb.create(DianmingActivity.this);
		for (int i = 0; i < stu.size(); i++) {
			QiandaoMessages qiandaoMessages = new QiandaoMessages();
			// TODO 这里只考虑了每天只给一个班级上一节课，更多节数的以后考虑
			qiandaoMessages.setDate(date);
			qiandaoMessages.setClassname(classname);
			qiandaoMessages.setCoursename(coursename);
			qiandaoMessages.setStunum(stu.get(i).getStudentnum());
			qiandaoMessages.setImei(stu.get(i).getImei());
			if (stu.get(i).getRemark() != null
					&& stu.get(i).getRemark().equals("yes")) {
				qiandaoMessages.setComeornot(true);
			} else {
				qiandaoMessages.setComeornot(false);
			}
			qiandaoDb.save(qiandaoMessages);
		}
		isDianmingIng = false;
		Toast.makeText(DianmingActivity.this, "点名结束", Toast.LENGTH_SHORT)
				.show();
	}

	// 设置点击学生按钮后执行的事件
	private void PutInStudent(int num) {
		if (state[num - 1] == false) {
			Message msg = new Message();
			msg.what = num;
			handler.sendMessage(msg);
			state[num - 1] = true;
			for (int i = 0; i < stu.size(); i++) {
				String thisnum = stu.get(i).getStudentnum();
				Log.d("TAG", thisnum + "  " + 5588);
				if (num == Integer
						.valueOf(thisnum.substring(thisnum.length() - 3))) {
					stu.get(i).setRemark("yes");
					stu.get(i).setImei("手动");
				}

			}
		} else {
			Message msg = new Message();
			msg.what = num;
			handler_no.sendMessage(msg);
			state[num - 1] = false;
			for (int i = 0; i < stu.size(); i++) {
				String thisnum = stu.get(i).getStudentnum();
				if (num == Integer
						.valueOf(thisnum.substring(thisnum.length() - 3))) {
					stu.get(i).setRemark("no");
					stu.get(i).setImei("手动");
				}
			}
		}
	}

	// 设置用户名和密码
	public void setHTCSSID(WifiConfiguration config) {
		try {
			Field mWifiApProfileField = WifiConfiguration.class
					.getDeclaredField("mWifiApProfile");
			mWifiApProfileField.setAccessible(true);
			Object hotSpotProfile = mWifiApProfileField.get(config);
			mWifiApProfileField.setAccessible(false);

			if (hotSpotProfile != null) {
				Field ssidField = hotSpotProfile.getClass().getDeclaredField(
						"SSID");
				ssidField.setAccessible(true);
				ssidField.set(hotSpotProfile, config.SSID);
				ssidField.setAccessible(false);

				Field localField3 = hotSpotProfile.getClass().getDeclaredField(
						"key");
				localField3.setAccessible(true);
				localField3.set(hotSpotProfile, config.preSharedKey);
				localField3.setAccessible(false);

				Field localField6 = hotSpotProfile.getClass().getDeclaredField(
						"dhcpEnable");
				localField6.setAccessible(true);
				localField6.setInt(hotSpotProfile, 1);
				localField6.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
