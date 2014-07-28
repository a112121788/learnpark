package net.learnpark.app.teacher.learnpark;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.learnpark.app.teacher.learnpark.net.ConnectState;
import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.Course;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.ByteUtil;
import net.learnpark.app.teacher.learnpark.util.CourseUtil;
import net.learnpark.app.teacher.learnpark.util.Setting;
import net.learnpark.app.teacher.learnpark.util.VibratorUtil;
import net.learnpark.app.teacher.learnpark.util.XmlUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SettingActivity extends ActionBarActivity {
	private String username;
	private Boolean IsConnected;
	private Boolean ivbnotice = false;
	private Boolean ivbvoice = false;
	private Boolean ivbvibrate = false;
	private Boolean lilbnotice = false;// 提醒设置菜单是默认隐藏的
	private Boolean lilbcourses = false;
	private Calendar c = Calendar.getInstance();
	private AlertDialog alertDialog_chioce;
	private AlertDialog alertDialog_version;
	private AlertDialog alertDialog_newversion;
	private String download_path;
	private String newversionnum;
	private String nowversionnum;
	private String download_t;

	private SharedPreferences sp;

	private ImageView Download_img;
	private RelativeLayout Download_rely;
	private ProgressBar CheckUpdate_prb;
	private ProgressBar Download_prb;
	private ImageView CheckUpdate_iv;
	private TextView CheckUpdate_tv;
	private TextView Download_tv;
	private Button btn_update;
	private int DownloadOK = 1;
	private String current;
	private String count;

	private int Progress;
	private Boolean IsDownloadOk = false;
	private Boolean IsDownloading = false;

	private HttpHandler<File> http_hander;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == DownloadOK) {
				Download_tv.setText("下载完成，请点击安装");
				Download_img.setVisibility(View.GONE);
				Setting.installApk(SettingActivity.this, download_path);
			}
		};
	};

	// 设置notice的监听事件
	public void changenotice(View v) {
		ImageView iv = (ImageView) v
				.findViewById(R.id.setting_imagebutton_notice);
		if (ivbnotice == true) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
			ivbnotice = false;
		} else if (ivbnotice == false) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
			ivbnotice = true;
		}
	}

	// 设置voice的监听事件
	public void changevoice(View v) {
		ImageView iv = (ImageView) v
				.findViewById(R.id.setting_imagebutton_voice);
		if (ivbvoice == true) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
			ivbvoice = false;
		} else if (ivbvoice == false) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
			ivbvoice = true;
		}
	}

	// 设置vibrate的监听事件
	public void changevibrate(View v) {
		ImageView iv = (ImageView) v
				.findViewById(R.id.setting_imagebutton_vibrate);
		if (ivbvibrate == true) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
			ivbvibrate = false;
		} else if (ivbvibrate == false) {
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
			ivbvibrate = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_setting);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("user_mail", "");
		download_t = sp.getString("download_t", "");
		download_path = sp.getString("download_path", "");
		newversionnum = sp.getString("newversionnum", "");
		nowversionnum = sp.getString("nowversionnum", "");
		IsDownloadOk = sp.getBoolean("IsDownloadOk", false);
		Progress = sp.getInt("Progress", 0);
		count = sp.getString("count", "");
		current = sp.getString("current", "1");

		// 将提醒设置按钮设置为保存的值
		ImageView ivnotice = (ImageView) findViewById(R.id.setting_imagebutton_notice);
		ImageView ivvoice = (ImageView) findViewById(R.id.setting_imagebutton_voice);
		ImageView ivvibrate = (ImageView) findViewById(R.id.setting_imagebutton_vibrate);
		// 初始化notice按钮的显示图片
		if (ivbnotice == true) {
			ivnotice.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
		} else if (ivbnotice == false) {
			ivnotice.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
		}
		// 初始化voice按钮的显示图片
		if (ivbvoice == true) {
			ivvoice.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
		} else if (ivbvoice == false) {
			ivvoice.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
		}
		// 初始化vibrate按钮的显示图片
		if (ivbvibrate == true) {
			ivvibrate.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_on));
		} else if (ivbvibrate == false) {
			ivvibrate.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_setting_off));
		}

		// 设置课程设置菜单的出现和消失
		RelativeLayout tvcourses = (RelativeLayout) findViewById(R.id.setting_courses_onoff);
		final LinearLayout lil_courses = (LinearLayout) findViewById(R.id.setting_courses_lilnotice);
		final ImageView ivcoursesonoff = (ImageView) findViewById(R.id.setting_image_courses_onoff);
		tvcourses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (lilbcourses == false) {
					lil_courses.setVisibility(View.VISIBLE);
					lilbcourses = true;
					ivcoursesonoff.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
				} else if (lilbcourses == true) {
					lil_courses.setVisibility(View.GONE);
					lilbcourses = false;
					ivcoursesonoff.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
				}
			}
		});

		// 设置课程监听里的页面跳转
		RelativeLayout relcoursesimport = (RelativeLayout) findViewById(R.id.setting_courses_import);
		RelativeLayout relcoursesexport = (RelativeLayout) findViewById(R.id.setting_courses_export);
		RelativeLayout relcoursesupload = (RelativeLayout) findViewById(R.id.setting_courses_uoload);
		relcoursesimport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_OK, (new Intent()).setAction(3 + ""));
				AlertDialog.Builder bulider = new Builder(SettingActivity.this);
				bulider.setIcon(R.drawable.ic_launcher);

				View layout = (View) LayoutInflater.from(SettingActivity.this)
						.inflate(R.layout.importcourses, null);
				bulider.setView(layout);

				Button btn_sd = (Button) layout
						.findViewById(R.id.importcourses_sd);
				Button btn_net = (Button) layout
						.findViewById(R.id.importcourses_net);

				btn_sd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						importcourse_sd();
						alertDialog_chioce.dismiss();
					}
				});
				btn_net.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						IsConnected = ConnectState
								.isNetworkConnected(SettingActivity.this);
						if (IsConnected == true) {
							if (username != null && username.length() > 0) {
								importcourse_net();
								alertDialog_chioce.dismiss();
							} else {
								Toast.makeText(SettingActivity.this, "请先登录帐号",
										Toast.LENGTH_SHORT).show();
								alertDialog_chioce.dismiss();
							}
						} else {
							Toast.makeText(SettingActivity.this,
									"没有网络连接，无法恢复课表", Toast.LENGTH_SHORT).show();
						}
					}
				});
				alertDialog_chioce = bulider.create();
				alertDialog_chioce.show();
			}
		});

		// 导出课程表到SD卡监听
		relcoursesexport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Course> list = CourseUtil
						.getCoursesList(SettingActivity.this);
				if (new XmlUtil().ExportCourse(list)) {
					Toast.makeText(SettingActivity.this,
							"备份成功,备份路径为:sdcard/learnpark/download/courses",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SettingActivity.this, "备份失败",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 备份课程表到网上
		relcoursesupload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 判断当前网络状态
				IsConnected = ConnectState
						.isNetworkConnected(SettingActivity.this);
				if (IsConnected == true) {
					if (username != null && username.length() > 0) {
						UpLoadCourses();
					} else {
						Toast.makeText(SettingActivity.this, "请先登陆帐号",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SettingActivity.this, "没有网络连接，无法上传",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 设置提醒设置菜单的出现和消失
		RelativeLayout tvnotice = (RelativeLayout) findViewById(R.id.setting_notice_onoff);
		final LinearLayout lil_notice = (LinearLayout) findViewById(R.id.setting_lilnotice);
		final ImageView ivnoticeonoff = (ImageView) findViewById(R.id.setting_image_onoff);
		tvnotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (lilbnotice == false) {
					lil_notice.setVisibility(View.VISIBLE);
					lilbnotice = true;
					ivnoticeonoff.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
				} else if (lilbnotice == true) {
					lil_notice.setVisibility(View.GONE);
					lilbnotice = false;
					ivnoticeonoff.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
				}

			}
		});

		// 调整提醒提前时间
		final TextView bt = (TextView) findViewById(R.id.setting_text_notice);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.setting_relbutton_notice);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				c.setTimeInMillis(System.currentTimeMillis());
				int mHour = 0;
				int mMinute = 30;
				new TimePickerDialog(SettingActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								c.setTimeInMillis(System.currentTimeMillis());
								c.set(Calendar.HOUR_OF_DAY, hourOfDay);
								c.set(Calendar.MINUTE, minute);
								c.set(Calendar.SECOND, 0); // 设为 0
								c.set(Calendar.MILLISECOND, 0); // 设为 0
								if (hourOfDay == 0) {
									bt.setText("提前" + Integer.toString(minute)
											+ "分钟");
								} else {
									bt.setText("提前"
											+ Integer.toString(hourOfDay)
											+ "小时" + Integer.toString(minute)
											+ "分钟");
								}
							}
						}, mHour, mMinute, true).show();
			}
		});

		Download_img = (ImageView) findViewById(R.id.imageview_update_download);
		Download_rely = (RelativeLayout) findViewById(R.id.rely_update_download);
		Download_tv = (TextView) findViewById(R.id.textview_update_download);
		Download_prb = (ProgressBar) findViewById(R.id.progressBar_update_download);
		btn_update = (Button) findViewById(R.id.setting_button_update);
		Button btn_about = (Button) findViewById(R.id.setting_button_about);

		if (!newversionnum.equals(nowversionnum)) {
			btn_update.setVisibility(View.GONE);
			Download_rely.setVisibility(View.VISIBLE);
			Log.d("TAG", count + "kkk" + current + "     versionnum" + 5588);
			Download_prb.setProgress(Progress);
			if (count.equals(current)) {
				File file=new File(download_path);
				if (file.exists()) {
					Download_tv.setText("下载完成，请点击安装");
					Download_img.setVisibility(View.GONE);
				}else {
					File file1=new File(new File(download_path).getParent());
					Log.d("TAG", new File(download_path).getParent()+"文件夹不存在，创建了     " + "  " + 5588);
					if (!file1.exists()) {
						Log.d("TAG", "文件夹不存在，创建了1     " + "  " + 5588);
						file1.mkdirs();
					}
					btn_update.setVisibility(View.VISIBLE);
					Download_rely.setVisibility(View.GONE);
				}
			} else {
				File file1=new File(new File(download_path).getParent()+"/");
				Log.d("TAG", new File(download_path).getParent()+"文件夹不存在，创建了     " + "  " + 5588);
				if (!file1.exists()) {
					Log.d("TAG", "文件夹不存在，创建了2     " + "  " + 5588);
					file1.mkdirs();
				}
				Download_tv.setText("有新版本请点击更新");
			}
		}

		btn_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});
		// 检查更新
		btn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder bulider = new Builder(SettingActivity.this);
				bulider.setIcon(R.drawable.ic_launcher);

				View layout = (View) LayoutInflater.from(SettingActivity.this)
						.inflate(R.layout.check_update, null);
				bulider.setView(layout);

				alertDialog_version = bulider.create();
				alertDialog_version.show();

				CheckUpdate_prb = (ProgressBar) layout
						.findViewById(R.id.progressBar_update_check);
				CheckUpdate_iv = (ImageView) layout
						.findViewById(R.id.check_update_imageview);
				CheckUpdate_tv = (TextView) layout
						.findViewById(R.id.check_update_textview);

				AjaxParams getnewversion_params = new AjaxParams();
				try {
					getnewversion_params.put("old_version", URLEncoder.encode(
							Setting.getVersionName(SettingActivity.this),
							"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				// 网络 验证用户信息
				FinalHttp fh_update = new FinalHttp();
				// 请求超时
				fh_update
						.getHttpClient()
						.getParams()
						.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
								5000);
				// 读取超时
				fh_update.getHttpClient().getParams()
						.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
				fh_update.post(NetCantants.GET_NEW_VERSION_URL,
						getnewversion_params, new AjaxCallBack<Object>() {
							@Override
							public void onStart() {

							};

							public void onLoading(long count, long current) {

							}

							@Override
							public void onSuccess(final Object t) {

								if (t != null) {
									if ("NONEWVERSION".equals(t)) {
										CheckUpdate_prb
												.setVisibility(View.GONE);
										CheckUpdate_iv
												.setVisibility(View.VISIBLE);
										CheckUpdate_tv.setText("已经是最新版本了");
										SharedPreferences.Editor editor = sp
												.edit();
										editor.putBoolean("IsDownloadOk", false);
										editor.commit();
									} else {

										download_path = Environment
												.getExternalStorageDirectory()
												.getPath()
												+ "/learnpark/download"
												+ ((String) t).substring(((String) t)
														.lastIndexOf("/"));
										download_t = (String) t;
										// 有新版本，提示下载新版本
										SharedPreferences.Editor editor = sp
												.edit();
										String versionnum = ((String) t).substring(
												((String) t).lastIndexOf("_") + 1,
												((String) t).lastIndexOf("."));
										editor.putString("newversionnum",
												versionnum);
										editor.putString("download_path",
												download_path);
										editor.putString("download_t",
												(String) t);
										editor.commit();

										btn_update.setVisibility(View.GONE);
										Download_rely
												.setVisibility(View.VISIBLE);
										Download_tv.setText("有新版本请点击更新");

										Log.d("TAG", versionnum
												+ "     versionnum" + 5588);
										alertDialog_version.dismiss();
										AlertDialog.Builder bulider = new Builder(
												SettingActivity.this);
										bulider.setIcon(R.drawable.ic_launcher);
										bulider.setTitle("发现新版本，是否下载");
										bulider.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface arg0,
															int arg1) {
														// 点击确定开始下载
														IsDownloading = true;
														alertDialog_newversion
																.dismiss();
														btn_update
																.setVisibility(View.GONE);
														Download_rely
																.setVisibility(View.VISIBLE);
														Download_img
																.setImageDrawable(getResources()
																		.getDrawable(
																				R.drawable.ic_updata_stop));
														DownNewVersion();
													}
												});

										bulider.setNegativeButton(
												"取消",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface arg0,
															int arg1) {
														// TODO Auto-generated
														// method stub
														alertDialog_newversion
																.dismiss();
													}
												});
										alertDialog_newversion = bulider
												.create();
										alertDialog_newversion.show();

									}
								}
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(SettingActivity.this, "网络连接异常",
										Toast.LENGTH_SHORT).show();
								alertDialog_version.dismiss();
							}
						});

			}
		});

		Download_rely.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("TAG", "点击了 " + "  " + 5588);
				if (IsDownloadOk == false) {
					if (IsDownloading == false) {
						IsDownloading = true;
						DownNewVersion();
						Download_img.setImageDrawable(getResources()
								.getDrawable(R.drawable.ic_updata_stop));
						Log.d("TAG", "开始下载了     " + "  " + 5588);
					} else {
						http_hander.stop();
						IsDownloading = false;
						Download_img.setImageDrawable(getResources()
								.getDrawable(R.drawable.ic_updata_start));
						Log.d("TAG", "停止下载了     " + "  " + 5588);
					}

				} else {
					File file = new File(download_path);
					if (file.exists()) {
						Message msg = new Message();
						// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
						msg.what = DownloadOK;
						handler.sendMessage(msg);
					} else {

						DownNewVersion();
					}
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	// TODO 可以把下面的函数写进线程里，增加体验效果
	// 从本地导入课程表
	public void importcourse_sd() {
		String filePath = Environment.getExternalStorageDirectory().getPath()
				+ "/learnpark/download/courses/Courses_01.xml";
		List<Course> mcourse = new XmlUtil().ImportCourse(filePath);
		if (mcourse != null && mcourse.size() > 0) {
			importcourse(mcourse);
		} else {
			// 设置震动
			VibratorUtil.Vibrate(SettingActivity.this, 200);
			Toast.makeText(SettingActivity.this, "您尚未备份过课程，无法恢复",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (IsDownloading == true) {
				IsDownloading = false;
				http_hander.stop();
				Log.d("TAG", "停止下载了     " + "  " + 5588);
				finish();
			} else {
				finish();
			}
		}
		return false;
	}

	// 从网上导入课程表
	private void importcourse_net() {

		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		final String username = sp.getString("user_mail", "");
		final String password = sp.getString("password", "");

		AjaxParams getuserinfo_params = new AjaxParams();
		try {
			getuserinfo_params.put("username",
					URLEncoder.encode(username, "utf-8"));
			getuserinfo_params.put("password",
					URLEncoder.encode(password, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// 网络 验证用户信息
		FinalHttp fh = new FinalHttp();
		// 请求超时
		fh.getHttpClient().getParams()
				.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		// 读取超时
		fh.getHttpClient().getParams()
				.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		fh.post(NetCantants.GET_COURSES_List_URL, getuserinfo_params,
				new AjaxCallBack<Object>() {
					@Override
					public void onStart() {
					};

					public void onLoading(long count, long current) {
					}

					@Override
					public void onSuccess(Object t) {

						if (t != null) {
							if ("ERROR".equals(t)) {
								Toast.makeText(SettingActivity.this,
										"用户名错误，请重新登陆", Toast.LENGTH_SHORT)
										.show();
							} else if ("PASSWORDERROR".equals(t)) {
								Toast.makeText(SettingActivity.this,
										"密码错误，请重新登陆", Toast.LENGTH_SHORT)
										.show();
							} else if ("NOCOURSESLIST".equals(t)) {
								Toast.makeText(SettingActivity.this,
										"您尚未在云端上传过课程表，无法恢复", Toast.LENGTH_SHORT)
										.show();
							} else {
								Gson gson = new Gson();
								Type typeOfT = new TypeToken<List<Course>>() {
								}.getType();
								List<Course> list_course = null;
								list_course = gson
										.fromJson((String) t, typeOfT);
								importcourse(list_course);
							}
						} else {
							VibratorUtil.Vibrate(SettingActivity.this, 200);
							Toast.makeText(SettingActivity.this,
									"您尚未备份过课程，无法恢复", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(SettingActivity.this, "网络连接异常，请稍后再试",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	private void importcourse(List<Course> mcourse) {
		if (mcourse != null && mcourse.size() > 0) {
			for (Course course : mcourse) {
				CourseUtil.insertCourses(SettingActivity.this, course);
			}
		}
		Toast.makeText(SettingActivity.this, "恢复成功", Toast.LENGTH_SHORT).show();
	}

	// 上传教师的课程表
	private void UpLoadCourses() {

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
					HttpPost post = new HttpPost(NetCantants.UPLOAD_COURSES_URL);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
					list.add(new BasicNameValuePair("name", URLEncoder.encode(
							username, "utf-8")));
					// 将签到信息转换成json数据
					java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Course>>() {
					}.getType();
					List<Course> courseslist = CourseUtil
							.getCoursesList(SettingActivity.this);
					String beanListToJson = new Gson()
							.toJson(courseslist, type);
					list.add(new BasicNameValuePair("courses", URLEncoder
							.encode(beanListToJson, "utf-8")));
					Log.d("TAG", beanListToJson + "  " + "beanListToJson");
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
						// TODO 这里可以加一条当前的连接信息
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
				if (result != null && result.equals("OK")) {
					Toast.makeText(SettingActivity.this, "上传成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SettingActivity.this, "上传失败",
							Toast.LENGTH_SHORT).show();
				}

			}
		};
		task.execute(NetCantants.UPLOAD_COURSES_URL);
	}

	// 下载更新文件
	private void DownNewVersion() {
		
		Log.d("TAG", "download_t     " + NetCantants.NET_BASE
				+ download_t + "  " + 5588);
		Log.d("TAG", "download_path     " + download_path + "  " + 5588);

		File file1=new File(new File(download_path).getParent());
		Log.d("TAG", new File(download_path).getParent()+"文件夹不存在，创建了     " + "  " + 5588);
		if (!file1.exists()) {
			Log.d("TAG", "文件夹不存在，创建了3     " + "  " + 5588);
			file1.mkdirs();
		}
		
		FinalHttp fh_download = new FinalHttp();
		http_hander = fh_download.download(NetCantants.NET_BASE + "/"
				+ download_t, download_path, true, new AjaxCallBack<File>() {
			public void onLoading(long count, long current) {
				Download_prb.setProgress((int) (100 * (current * 1.0 / count)));
				Log.d("TAG", (int) (100.0 * (current * 1.0 / count))
						+ "  onloading" + 7);
				Download_tv
						.setText((int) (100 * (current * 1.0 / count)) + "%");

				// 将当前的下载进度存如sp内
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("Progress", (int) (100 * (current * 1.0 / count)));
				editor.putString("count", count + "");
				editor.putString("current", current + "");
				editor.commit();

				Log.d("TAG", "download_t     " + NetCantants.NET_BASE + "/"
						+ download_t + "  " + 5588);
				Log.d("TAG", "download_path     " + download_path + "  " + 5588);

				// 这里如果下载完成调用函数
				if (count == current) {
					editor.putBoolean("IsDownloadOk", true);
					editor.commit();
					IsDownloading = false;
					IsDownloadOk = true;
					Message msg = new Message();
					// Log.d("TAG", thisname + "     " + Num + "  " +
					// 5588);
					msg.what = DownloadOK;
					handler.sendMessage(msg);
				} else {
					editor.putBoolean("IsDownloadOk", false);
					editor.commit();
					IsDownloadOk = false;
				}
			};

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {

				if (IsDownloading == true) {
					Download_img.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_updata_start));
					Toast.makeText(SettingActivity.this, "网络连接异常，下载失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
