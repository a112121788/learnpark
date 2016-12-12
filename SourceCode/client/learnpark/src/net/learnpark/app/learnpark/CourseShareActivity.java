package net.learnpark.app.learnpark;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.learnpark.app.learnpark.entity.Course;
import net.learnpark.app.learnpark.util.FileUtil;
import net.learnpark.app.learnpark.util.QRCodeUtil;
import net.learnpark.app.learnpark.util.Setting;
import net.learnpark.app.library.barcode.core.CaptureActivity;
import net.learnpark.app.wifishare.CreateFile;
import net.learnpark.app.wifishare.HttpServer;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;

/**
 * 课程共享
 * 
 * @author peng
 * @version 1 2014年6月5日 15:41:55
 */

public class CourseShareActivity extends ActionBarActivity {
	Button share_course;
	Button get_course;
	ImageView course_qrcode;

	private boolean server_status = false;
	private String WEB_ROOT;
	private String HOST_ADDR;
	private HttpServer httpServer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses_share);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("课表共享");
		SysApplication.getInstance().addActivity(this); 
		// ActionBar的设置
		Setting.getOverflowMenu(this);
		onInitNetAndFile();
		share_course = (Button) findViewById(R.id.share_course);
		get_course = (Button) findViewById(R.id.get_course);
		course_qrcode = (ImageView) findViewById(R.id.course_qrcode);

		// 查询课程数据
		CourseSQLiteOpenHelper courseHelper = new CourseSQLiteOpenHelper(
				getApplicationContext());
		SQLiteDatabase coursesDb = courseHelper.getWritableDatabase();
		String sql = "select * from courses  order by day,timebegin";
		Cursor cursor = coursesDb.rawQuery(sql, new String[] {});
		List<Course> list = new ArrayList<Course>();
		while (cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			int day = Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("day")));
			int time = cursor.getInt(cursor.getColumnIndex("time"));
			String coursename = cursor.getString(cursor
					.getColumnIndex("coursename"));
			String timebegin = cursor.getString(cursor
					.getColumnIndex("timebegin"));
			String timeend = cursor.getString(cursor.getColumnIndex("timeend"));
			String cite = cursor.getString(cursor.getColumnIndex("cite"));
			String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
			Course course = new Course(id, day, time, coursename, timebegin,
					timeend, cite, teacher);
			list.add(course);
		}
		String coursegson = new Gson().toJson(list);
		// 备份到本地
		FileUtil.str2File(coursegson, "/mnt/sdcard/learnpark/courses.txt");
		share_course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = null;
				boolean isChecked = true;
				if (isChecked && server_status) {
					onInitServer(WEB_ROOT, HOST_ADDR);
					url = "http://" + HOST_ADDR + ":3693/courses.txt";
					server_status = false;
				} else {
					if (httpServer != null) {
						try {
							httpServer.serverSocket.close();
						} catch (IOException e) {
						}
						server_status = true;
						// ("服务器准备就绪。");
					}
				}

				Bitmap bitmap = null;
				try {
					bitmap = QRCodeUtil.Create2DCode(url + "");
				} catch (WriterException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if (bitmap != null) {
					course_qrcode.setImageBitmap(bitmap);
				}
				course_qrcode.setVisibility(View.VISIBLE);

			}

			private void onInitServer(final String web_root,
					final String host_addr) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						httpServer = new HttpServer(web_root, host_addr);
						httpServer.await();
					}
				}).start();
			}
		});
		get_course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CaptureActivity.class);
				startActivity(intent);
			}
		});

	}

	public void onInitNetAndFile() {
		String sdp = getSDPath();
		String ha = getLocalIpAddress();
		if (sdp != null && ha != null) {
			WEB_ROOT = sdp + "/learnpark";
			HOST_ADDR = ha;
			if (CreateFile.createDir(WEB_ROOT)) {
				server_status = true;
				Toast.makeText(getApplicationContext(), "课表准备就绪。", 2).show();
			} else {
				server_status = false;
				Toast.makeText(getApplicationContext(), "创建文件失败.", 2).show();
			}
		} else {
			server_status = false;
			Toast.makeText(getApplicationContext(), "请确认你的手机连上了wifi", 2).show();
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