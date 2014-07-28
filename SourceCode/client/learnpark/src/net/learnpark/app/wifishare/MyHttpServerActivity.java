package net.learnpark.app.wifishare;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import net.learnpark.app.learnpark.R;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MyHttpServerActivity extends Activity {

	private TextView msgTV;
	private ToggleButton but_start_http;

	private boolean server_status = false;
	private String WEB_ROOT;
	private String HOST_ADDR;

	private HttpServer httpServer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifishare);
		onInit();
	}

	@Override
	protected void onDestroy() {
		try {
			if (httpServer != null && httpServer.serverSocket != null
					&& !httpServer.serverSocket.isClosed()) {
				httpServer.serverSocket.close();
			}
		} catch (IOException e) {
		}
		super.onDestroy();
	}

	public void onInit() {
		onInitView();
		onInitNetAndFile();
	}

	public void onInitView() {
		msgTV = (TextView) findViewById(R.id.server_msg);
		but_start_http = (ToggleButton) findViewById(R.id.but_start_http);
		but_start_http
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked && server_status) {
							onInitServer(WEB_ROOT, HOST_ADDR);
							msgTV.setText("http://" + HOST_ADDR + ":3693/");
							server_status = false;
						} else {
							if (httpServer != null) {
								try {
									httpServer.serverSocket.close();
								} catch (IOException e) {
								}
								server_status = true;
								msgTV.setText("服务器准备就绪。");
							}
						}

					}

				});
	}

	public void onInitNetAndFile() {
		String sdp = getSDPath();
		String ha = getLocalIpAddress();
		if (sdp != null && ha != null) {
			WEB_ROOT = sdp + "/eyeths";
			HOST_ADDR = ha;
			if (CreateFile.createDir(WEB_ROOT)) {
				server_status = true;
				msgTV.setText("服务器准备就绪。");
			} else {
				server_status = false;
				msgTV.setText("创建文件失败.");
			}
		} else {
			server_status = false;
			msgTV.setText("没有内存卡 或者没有ip");
		}
	}

	public void onInitServer(final String web_root, final String host_addr) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				httpServer = new HttpServer(web_root, host_addr);
				httpServer.await();
			}
		}).start();

	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}
		return null;

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}
}