package net.learnpark.app.learnpark.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class CheckinUtil {
	// wifi当前的状态
	public static final int WIFI_AP_STATE_DISABLING = 0;
	public static final int WIFI_AP_STATE_DISABLED = 1;
	public static final int WIFI_AP_STATE_ENABLING = 2;
	public static final int WIFI_AP_STATE_ENABLED = 3;
	public static final int WIFI_AP_STATE_FAILED = 4;

	public static Boolean closeWifiAP(WifiManager mWifiManager) {
		// 这里要判断一下wifi热点是否开启，如果开启就关闭它
		int wifiap = getWifiApState(mWifiManager) % 10;
		// Log.d("TAG", "wifiap"+wifiap + "  " + 88888);
		if (wifiap == 2 || wifiap == 3) {
			try {
				Method method = mWifiManager.getClass().getMethod(
						"setWifiApEnabled", WifiConfiguration.class,
						boolean.class);
				return (Boolean) method.invoke(mWifiManager, null, false);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	// 得到wifi热点当前的状态
	public static int getWifiApState(WifiManager mWifiManager) {
		try {
			Method method = mWifiManager.getClass().getMethod("getWifiApState");
			return (Integer) method.invoke(mWifiManager);
		} catch (Exception e) {
			e.printStackTrace();
			return WIFI_AP_STATE_FAILED;
		}
	}

	public static void openWifi(WifiManager mWifiManager) {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭连接，输入流，输出流
	public static void CloseConnect(Socket socket, BufferedReader in,PrintWriter out) {

		try {
			if (socket != null) {
				socket.close();
				Log.d("TAG", "关闭了111" + "  " + 88);
			}
			if (in != null) {
				in.close();
				Log.d("TAG", "关闭了222" + "  " + 88);
			}
			if (out != null) {
				out.close();
				Log.d("TAG", "关闭了333" + "  " + 88);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// 添加一个网络并连接
	public static void addNetwork(WifiManager mWifiManager,WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		System.out.println("a--" + wcgID);
		System.out.println("b--" + b);
	}

	// 断开指定ID的网络
	public static void disconnectWifi(WifiManager mWifiManager,int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
}
