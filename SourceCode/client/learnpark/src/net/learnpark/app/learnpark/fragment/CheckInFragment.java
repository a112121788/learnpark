package net.learnpark.app.learnpark.fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import net.learnpark.app.learnpark.R;
import net.learnpark.app.learnpark.entity.Teacher;
import net.learnpark.app.learnpark.util.CheckinUtil;
import net.learnpark.app.learnpark.util.DeviceInfoUtil;
import net.learnpark.app.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 在主界面点击签到时的显示内容
 * 
 * @author peng
 */
public class CheckInFragment extends Fragment {
	TextView tv;

	private Button bt;
	private String SSID = null;// wifi名称
	private String PASSWORD = null;// wifi密码
	private int Type = 3;
	private WifiConfiguration config;
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private ConnectivityManager connectivityManager;
	private List<WifiConfiguration> existingConfigs;
	private NetworkInfo networkInfo;
	private WifiInfo mWifiInfo;
	private DhcpInfo mDhcpInfo;
	private static final int QIANDAO_OK = 1;
	private static final int QIANDAO_SEND = 2;
	private static final int QIANDAO_FAIL = 0;
	private static final int QIANDAO_CONNECTFAIL = 3;
	private static final int GETMESSAGE = 1;
	private static final int CONNECT_FAIL = 4;
	private Boolean SUM1;
	private Boolean SUM2;
	private Boolean SUM3;
	private static String ServerAddress;
	private static int Port = 55555;
	private String mymessage = null; // 学生信息
	Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String returnmessage = "no message";

	Thread sendmsg;
	Thread myThread_getconnect;
	Thread myThread_getserveraddress;
	// wifi当前的状态
	public static final int WIFI_AP_STATE_DISABLING = 0;
	public static final int WIFI_AP_STATE_DISABLED = 1;
	public static final int WIFI_AP_STATE_ENABLING = 2;
	public static final int WIFI_AP_STATE_ENABLED = 3;
	public static final int WIFI_AP_STATE_FAILED = 4;
	// 这里是显示当前的签到状态的
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == QIANDAO_OK) {
				bt.setText("签到成功");
				progressBar.setVisibility(View.INVISIBLE);
			} else if (msg.what == QIANDAO_SEND) {
				bt.setText("连接成功，正在签到");
				progressBar.setVisibility(View.INVISIBLE);

			} else if (msg.what == CONNECT_FAIL) {
				bt.setText("连接超时，请重试");
				progressBar.setVisibility(View.INVISIBLE);
			} else if (msg.what == QIANDAO_CONNECTFAIL) {
				progressBar.setVisibility(View.INVISIBLE);
				bt.setText("连接失败，请重试");
			} else if (msg.what == QIANDAO_FAIL) {
				progressBar.setVisibility(View.INVISIBLE);
				bt.setText("签到超时，请重试");
			}
		};
	};

	// 这里是测试，是否收到服务端是否发回了信息的
	private Handler handler2 = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Log.d("TAG", returnmessage + "5  " + 88);
			if (msg.what == GETMESSAGE) {
			}
		};
	};

	ProgressBar progressBar;
	FinalDb teacherDb;
	Spinner teachermail_spinner;
	List<Teacher> teachersdata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_checkin,
				container, false);
		// 签到监听按钮
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		bt = (Button) view.findViewById(R.id.button1);
		teachermail_spinner = (Spinner) view
				.findViewById(R.id.teachermail_spinner);
		teacherDb = FinalDb.create(getActivity());
		teachersdata = teacherDb.findAll(Teacher.class);
		String[] teachermail_items = new String[teachersdata.size()];
		for (int i = 0; i < teachermail_items.length; i++) {
			teachermail_items[i] = teachersdata.get(i).getTeachername() + "--"
					+ teachersdata.get(i).getTeacheremail();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1, teachermail_items);
		teachermail_spinner.setAdapter(adapter);
		if (!getActivity()
				.getSharedPreferences("setting", Context.MODE_PRIVATE)
				.getString("user_number", "0").equals("0")) {

			mymessage = DeviceInfoUtil.getIMEI(getActivity())
					+ "#"
					+ getActivity().getSharedPreferences("setting",
							Context.MODE_PRIVATE).getString("user_number", "0");
		}
		teachermail_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						SSID = null;
						PASSWORD = null;
						if (teachersdata.get(position).getTeacheremail()
								.contains("@")) {

							String email = teachersdata
									.get(position)
									.getTeacheremail()
									.substring(
											0,
											teachersdata.get(position)
													.getTeacheremail()
													.lastIndexOf("@"));
							SSID = "lp" + email;
							PASSWORD = "a" + email + "a";// wifi密码
						} else {
							Toast.makeText(getActivity(), "邮箱格式不正确", 2).show();
							VibratorUtil.Vibrate(getActivity(), 500);
							return;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (SSID == null || PASSWORD == null) {
					Toast.makeText(getActivity(), "请选择老师", 2).show();
					VibratorUtil.Vibrate(getActivity(), 500);
					return;
				}
				if (mymessage == null) {
					Toast.makeText(getActivity(), "请输完善个人信息(学号)", 2).show();
					return;
				}
				Log.i("CHECKIN", SSID);
				Log.i("CHECKIN", PASSWORD);
				Log.i("CHECKIN", mymessage);
				SUM1 = false;
				SUM2 = false;
				SUM3 = false;
				tv = (TextView) view.findViewById(R.id.textView1);
				bt.setText("正在连接");
				progressBar.setVisibility(View.VISIBLE);
				connectivityManager = (ConnectivityManager) getActivity()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				mWifiManager = (WifiManager) getActivity().getSystemService(
						Context.WIFI_SERVICE);
				networkInfo = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				mWifiInfo = mWifiManager.getConnectionInfo();

				// 这里要判断一下wifi热点是否开启，如果开启就关闭它
				CheckinUtil.closeWifiAP(mWifiManager);
				mWifiManager.setWifiEnabled(true);
				// 打开wifi
				CheckinUtil.openWifi(mWifiManager);
				// 设置wifi配置信息
				config = new WifiConfiguration();
				config.allowedAuthAlgorithms.clear();
				config.allowedGroupCiphers.clear();
				config.allowedKeyManagement.clear();
				config.allowedPairwiseCiphers.clear();
				config.allowedProtocols.clear();
				config.SSID = "\"" + SSID + "\"";
				// 对当前的网络列表进行查询，如果要连接的网络已经存在就删除它，并重新连接
				myThread_getconnect = new Thread() {
					@Override
					public void run() {
						try {
							for (int i = 0; i < 20; i++) {
								Thread.sleep(1000);
								existingConfigs = mWifiManager
										.getConfiguredNetworks();
								if (existingConfigs == null) {
									continue;
								} else {
									for (WifiConfiguration existingConfig : existingConfigs) {
										if (existingConfig.SSID.equals("\""
												+ SSID + "\"")) {
											mWifiManager
													.removeNetwork(existingConfig.networkId);
											SUM1 = true;
											break;
										}
									}
									if (SUM1 == true) {
										break;
									}
								}
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 判断网络连接模式
						if (Type == 1) // WIFICIPHER_NOPASS
						{
							config.wepKeys[0] = "";
							config.allowedKeyManagement
									.set(WifiConfiguration.KeyMgmt.NONE);
							config.wepTxKeyIndex = 0;
						}
						if (Type == 2) // WIFICIPHER_WEP
						{
							config.hiddenSSID = true;
							config.wepKeys[0] = "\"" + PASSWORD + "\"";
							config.allowedAuthAlgorithms
									.set(WifiConfiguration.AuthAlgorithm.SHARED);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.CCMP);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.TKIP);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.WEP40);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.WEP104);
							config.allowedKeyManagement
									.set(WifiConfiguration.KeyMgmt.NONE);
							config.wepTxKeyIndex = 0;
						}
						if (Type == 3) // WIFICIPHER_WPA
						{
							config.preSharedKey = "\"" + PASSWORD + "\"";
							config.hiddenSSID = true;
							config.allowedAuthAlgorithms
									.set(WifiConfiguration.AuthAlgorithm.OPEN);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.TKIP);
							config.allowedKeyManagement
									.set(WifiConfiguration.KeyMgmt.WPA_PSK);
							config.allowedPairwiseCiphers
									.set(WifiConfiguration.PairwiseCipher.TKIP);
							// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
							config.allowedGroupCiphers
									.set(WifiConfiguration.GroupCipher.CCMP);
							config.allowedPairwiseCiphers
									.set(WifiConfiguration.PairwiseCipher.CCMP);
							config.status = WifiConfiguration.Status.ENABLED;
						}
						CheckinUtil.addNetwork(mWifiManager, config);
						// 这里建立连接后，就开始获取服务端的IP，并发送签到信息
						myThread_getserveraddress.start();

					}
				};
				// 获取教师端的IP，并判断是否连接了教师端，发送签到信息
				myThread_getserveraddress = new Thread() {

					@Override
					public void run() {
						for (int i = 0; i < 30; i++) {
							try {
								Thread.sleep(500);
								if (myThread_getconnect.isAlive()) {
									continue;
								}
								networkInfo = connectivityManager
										.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
								if (networkInfo.isConnected()) {
									mWifiInfo = mWifiManager
											.getConnectionInfo();
									mDhcpInfo = mWifiManager.getDhcpInfo();
									Log.d("TAG", "连接上了" + "  " + 888);
									if (mWifiInfo.getSSID().equals(
											"\"" + SSID + "\"")) {
										// 判断现在的连接信息，并根据信息更新界面
										Message msg = new Message();
										msg.what = QIANDAO_SEND;
										handler.sendMessage(msg);

										// Log.d("TAG", "真的连接上了" + "  " + 888);
										ServerAddress = Formatter
												.formatIpAddress(mDhcpInfo.serverAddress);
										// Log.d("TAG", ServerAddress + "  " +
										// 88);
										SUM2 = true;
									} else {
										Message msg = new Message();
										msg.what = QIANDAO_CONNECTFAIL;
										handler.sendMessage(msg);
									}
									break;
								} else {

								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// Log.d("TAG", SUM1 + "  " + SUM2 + 888);
						Message msg = new Message();
						if (SUM1 == false || SUM2 == false) {
							msg.what = CONNECT_FAIL;
							handler.sendMessage(msg);
							// Log.d("TAG", SUM1 + "  " + SUM2 + 888);
						} else {
							Log.d("TAG", "发送了" + "  " + 888);
							sendmsg.start();
							// Log.d("TAG", "真的发送了" + "  " + 888);
						}

					}
				};
				// 创建一个发送信息的线程
				sendmsg = new Thread() {
					@Override
					public void run() {
						super.run();
						try {
							socket = new Socket(ServerAddress, Port);
							in = new BufferedReader(new InputStreamReader(
									socket.getInputStream()));
							out = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(socket
											.getOutputStream())), true);
							// 发送信息的
							if (socket.isConnected()) {
								if (!socket.isOutputShutdown()) {
									out.println(mymessage + "\n");

									// Log.d("TAG", "发送了mymessage" + "  " + 88);
								}

								// 如果发送成功，服务器回复的信息
								for (int i = 0; i < 5; i++) {
									// 这里让他最多等五秒，如果五秒内服务端没回信息，则说明签到没有成功
									if ((returnmessage = in.readLine())
											.equals("I have got your message")) {
										Message msg = new Message();
										msg.what = QIANDAO_OK;
										handler.sendMessage(msg);
										SUM3 = true;
										msg = new Message();
										msg.what = GETMESSAGE;
										handler2.sendMessage(msg);
										Log.d("TAG", "接收了mymessage"
												+ returnmessage + "  " + 88);
										break;
									} else {
										Thread.sleep(1000);
									}
								}
								// 5秒内没有回信息，就说明，签到失败
								if (SUM3 == false) {
									Message msg = new Message();
									msg.what = QIANDAO_FAIL;
									handler.sendMessage(msg);
								}

							}

							// 关闭连接，输入流，输出流
							CheckinUtil.CloseConnect(socket, in, out);
						} catch (IOException ex) {
							ex.printStackTrace();

							// ShowDialog("login exception" + ex.getMessage());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};

				// 判断是否已经连接网络，并且连接的网络是什么网络
				if (networkInfo.isConnected()) {
					if (!mWifiInfo.getSSID().equals("\"" + SSID + "\"")) {
						mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
						mWifiManager.disconnect();
						myThread_getconnect.start();
					} else {
						SUM1 = true;
						Message msg = new Message();
						msg.what = QIANDAO_SEND;
						handler.sendMessage(msg);
						myThread_getserveraddress.start();
					}

				} else {
					myThread_getconnect.start();
				}
			}
		});
		return view;
	}
}
