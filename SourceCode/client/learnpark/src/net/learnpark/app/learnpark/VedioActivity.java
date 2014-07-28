package net.learnpark.app.learnpark;

import java.io.File;
import java.io.IOException;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 视频录制
 */
public class VedioActivity extends ActionBarActivity {

	private SurfaceView surfaceView;
	private Button luXiang_bt;
	private Button tingZhi_bt;
	private TextView time_tv; // 显示时间的文本框
	private MediaRecorder mRecorder;
	private boolean recording; // 记录是否正在录像,fasle为未录像, true 为正在录像
	private File videoFolder; // 存放视频的文件夹
	private File videFile; // 视频文件
	private Handler handler;
	private int time; // 时间
	private Camera myCamera; // 相机声明
	private Button save;
	private SurfaceHolder holder;
	/**
	 * 录制过程中,时间变化
	 */
	private Runnable timeRun = new Runnable() {

		@Override
		public void run() {
			time++;
			time_tv.setText(time + "秒");
			handler.postDelayed(timeRun, 1000);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		// 强制横屏
		setContentView(R.layout.activity_video);
		getSupportActionBar().hide();// 隐藏ActionBar
		SysApplication.getInstance().addActivity(this); 
		// 获取控件
		surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		luXiang_bt = (Button) findViewById(R.id.luXiang_bt);
		tingZhi_bt = (Button) findViewById(R.id.tingZhi_bt);
		time_tv = (TextView) findViewById(R.id.time);

		handler = new Handler();
		holder = surfaceView.getHolder();

		// 判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		System.out.println("sdCard存在: " + sdCardExist);

		if (sdCardExist) {
			// 设定存放视频的文件夹的路径
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "learnpark/video"
					+ File.separator;
			// 声明存放视频的文件夹的File对象
			videoFolder = new File(path);
			// 如果不存在此文件夹,则创建
			if (!videoFolder.exists()) {
				videoFolder.mkdirs();
			}

			// 设置surfaceView不管理的缓冲区
			surfaceView.getHolder().setType(
					SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			// 设置surfaceView分辨率
			surfaceView.getHolder().setFixedSize(800, 480);

			// 录像点击事件

			luXiang_bt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!recording) {
						try {
							// 关闭预览并释放资源
							myCamera.stopPreview();
							myCamera.release();
							myCamera = null;

							mRecorder = new MediaRecorder();
							// 获取当前时间,作为视频文件的文件名
							String nowTime = java.text.MessageFormat.format(
									"{0,date,yyyyMMdd_HHmmss}",
									new Object[] { new java.sql.Date(System
											.currentTimeMillis()) });
							// 声明视频文件对象
							videFile = new File(videoFolder.getAbsoluteFile()
									+ File.separator + nowTime + ".mp4");
							// 创建此视频文件
							videFile.createNewFile();
							System.out.println("视频文件: "
									+ videFile.getAbsolutePath());

							mRecorder.setPreviewDisplay(surfaceView.getHolder()
									.getSurface()); // 预览
							mRecorder
									.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
							mRecorder
									.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
							mRecorder
									.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 输出格式为mp4
							/* 引用android.util.DisplayMetrics 获取分辨率 */
							// DisplayMetrics dm = new DisplayMetrics();
							// getWindowManager().getDefaultDisplay().getMetrics(dm);
							mRecorder.setVideoSize(800, 480); // 视频尺寸
							mRecorder.setVideoFrameRate(30); // 视频帧频率
							mRecorder
									.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); // 视频编码
							mRecorder
									.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码
							mRecorder.setMaxDuration(1800000);
							mRecorder.setOutputFile(videFile.getAbsolutePath());
							mRecorder.prepare(); // 准备录像
							mRecorder.start(); // 开始录像
							time_tv.setVisibility(View.VISIBLE); // 设置文本框可见
							handler.post(timeRun); // 调用Runable
							recording = true; // 改变录制状态为正在录制
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					} else
						Toast.makeText(VedioActivity.this, "视频正在录制中...",
								Toast.LENGTH_LONG).show();
				}

			});
		} else
			Toast.makeText(this, "未找到sdCard!", Toast.LENGTH_LONG).show();

		// 停止点击事件
		tingZhi_bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (recording) {
					mRecorder.stop();
					mRecorder.release();
					handler.removeCallbacks(timeRun);
					time_tv.setVisibility(View.GONE);
					int videoTimeLength = time;
					time = 0;
					recording = false;
					Toast.makeText(
							VedioActivity.this,
							videFile.getAbsolutePath() + "  " + videoTimeLength
									+ "秒", Toast.LENGTH_LONG).show();
				}
				// 开启相机
				if (myCamera == null) {
					myCamera = Camera.open();
					try {
						myCamera.setPreviewDisplay(holder);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				myCamera.startPreview(); // 开启预览
			}
		});

		// 添加回调
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				System.out.println("------surfaceCreated------");
				// 开启相机
				if (myCamera == null) {
					myCamera = Camera.open();
					try {
						myCamera.setPreviewDisplay(holder);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				System.out.println("------surfaceChanged------");
				// 开始预览
				myCamera.startPreview();

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				System.out.println("------surfaceDestroyed------");
				// 关闭预览并释放资源
				if (myCamera != null) {
					myCamera.stopPreview();
					myCamera.release();
					myCamera = null;
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacks(timeRun);
		if (mRecorder != null) {
			mRecorder.release();
		}
		if (myCamera != null) {
			myCamera.stopPreview();
			myCamera.release();
		}
		super.onDestroy();
	}

}
