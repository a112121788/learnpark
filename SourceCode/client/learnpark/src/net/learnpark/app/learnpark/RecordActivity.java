package net.learnpark.app.learnpark;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends ActionBarActivity implements
		OnClickListener, RecordHelper.OnStateChangedListener {
	private RecordHelper mRecorder;// 录音类，实现录音功能
	private Button btnStart;// 开始录音按钮
	private Button btnStop;// 停止录音按钮
	private TextView text_one;// 显示已录音时间
	WakeLock mWakeLock;// 用WakeLock请求休眠锁，让其不会休眠
	Boolean flag = true;
	int s = 0;
	int s1 = 0;
	int m = 0;
	int m1 = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		setupViews();
	}

	// 初始化
	public void setupViews() {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"SoundRecorder");// 请求休眠锁
		mRecorder = new RecordHelper();
		mRecorder.setOnStateChangedListener(this);
		// 根据ID找到相应控件
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnStop.setClickable(false);
	}

	// 按钮的监听事件
	@Override
	public void onClick(View v) {
		LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout2);
		switch (v.getId()) {
		// 开始录音按钮的监听事件
		case R.id.btnStart:
			// 当点击开始录音按钮时显示已录音时间的textview
			linear.setVisibility(View.VISIBLE);
			// 暂停与开始录音按钮的切换
			handler.post(stard);
			// 扫描录音文件
			mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
					".mp3", this);
			// 设置停止录音按钮为可点击状态
			btnStop.setClickable(true);
			btnStart.setClickable(false);

			break;
		// 停止录音事件监听
		case R.id.btnStop:
			// 设置开始录音按钮为可点击状态
			btnStart.setClickable(true);
			flag = true;
			// 设置停止录音按钮为不可点击状态
			btnStop.setClickable(false);
			// 当点击停止录音按钮时将已录音时间隐藏掉
			linear.setVisibility(View.INVISIBLE);
			// 设置显示已录音时间
			text_one.setText("" + 0 + 0 + ":" + 0 + 0);
			s = 0;
			s1 = 0;
			m = 0;
			m1 = 0;
			handler.removeCallbacks(stard);
			mRecorder.stop();
			Toast.makeText(getApplicationContext(),
					"文件存储位置:\n/sdcard/learnpark/文件夹里", 2).show();
			break;

		}
	}

	@Override
	public void onStateChanged(int state) {
		if (state == RecordHelper.PLAYING_STATE
				|| state == RecordHelper.RECORDING_STATE) {
			mWakeLock.acquire(); // 录音的时候让其不休眠

		} else {
			if (mWakeLock.isHeld())
				mWakeLock.release();
		}
	}

	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
	}

	Handler handler = new Handler();
	Runnable stard = new Runnable() {
		// 将要执行的操作写在线程对象的run方法当中
		public void run() {

			if (m1 < 6) {
				s++;
				if (s == 10) {
					s = 0;
					s1++;
				} else if (s1 == 6 && s == 0) {
					m++;
					s = 0;
					s1 = 0;
				} else if (m == 10 && s1 == 0 && s == 0) {
					s = 0;
					s1 = 0;
					m = 0;
					m1++;

				}

			}
			text_one = (TextView) findViewById(R.id.text_one);
			text_one.setText("" + m1 + m + ":" + s1 + s);
			// 调用Handler的postDelayed()方法
			// 这个方法的作用是：将要执行的线程对象放入到队列当中，待时间结束后，运行制定的线程对象
			// 第一个参数是Runnable类型：将要执行的线程对象
			// 第二个参数是long类型：延迟的时间，以毫秒为单位
			handler.postDelayed(stard, 1000);
		}
	};

	/**
	 * onConfigurationChanged the package:android.content.res.Configuration.
	 * 
	 * @param newConfig
	 *            , The new device configuration.
	 *            当设备配置信息有改动（比如屏幕方向的改变，实体键盘的推开或合上等）时，
	 *            并且如果此时有activity正在运行，系统会调用这个函数。
	 *            注意：onConfigurationChanged只会监测应用程序在AnroidMainifest.xml中通过
	 *            android:configChanges="xxxx"指定的配置类型的改动；
	 *            而对于其他配置的更改，则系统会onDestroy()当前Activity，然后重启一个新的Activity实例。
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// 检测屏幕的方向：纵向或横向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 当前为横屏， 在此处添加额外的处理代码
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 当前为竖屏， 在此处添加额外的处理代码
		}
		// 检测实体键盘的状态：推出或者合上
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			// 实体键盘处于推出状态，在此处添加额外的处理代码
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			// 实体键盘处于合上状态，在此处添加额外的处理代码
		}
	}
}
