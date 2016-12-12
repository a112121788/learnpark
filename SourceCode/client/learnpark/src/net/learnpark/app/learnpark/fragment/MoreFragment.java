package net.learnpark.app.learnpark.fragment;

import java.util.Calendar;
import java.util.TimeZone;

import net.learnpark.app.learnpark.AlarmReceiver;
import net.learnpark.app.learnpark.LoginActivity;
import net.learnpark.app.learnpark.MessageActivity;
import net.learnpark.app.learnpark.PhotoActivity;
import net.learnpark.app.learnpark.R;
import net.learnpark.app.learnpark.RecordActivity;
import net.learnpark.app.learnpark.SaveCalendarActivity;
import net.learnpark.app.learnpark.SaveCourseActivity;
import net.learnpark.app.learnpark.TimerActivity;
import net.learnpark.app.learnpark.UserCenterActivity;
import net.learnpark.app.learnpark.VedioActivity;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 更多的
 * 
 * @author peng
 * 
 */
@SuppressLint("ValidFragment")
public class MoreFragment extends Fragment {
	Button video_recoder_btn;
	Button timer;
	Button photo;
	Button audio_record;
	TextView user_mail;
	ImageView user_photo;
	ImageView remind_voice;
	ImageView remind_vibrate;
	TextView calendar;
	TextView course;
	TextView tools;
	TextView course_remind;
	TextView share_course_to;
	TextView message;
	LinearLayout tools_all;
	LinearLayout course_remind_all;
	SeekBar remindtime_seekbar;
	TextView remindtime;
	SharedPreferences sp_voice;
	SharedPreferences.Editor editor_voice;// 闹铃
	SharedPreferences sp_vibrate;
	SharedPreferences.Editor editor_vibrate;// 震动
	boolean check_clock;// 闹铃的开关控制量
	boolean check_vibrate;// 震动图片的控制量
	boolean check_onOroff;// 震动的开关控制量
	int clock_time;// 闹铃提前的时间

	// TextView message; 消息盒子 暂时不做

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, container, false);

		user_photo = (ImageView) view.findViewById(R.id.user_photo);
		remind_voice = (ImageView) view.findViewById(R.id.remind_voice);
		remind_vibrate = (ImageView) view.findViewById(R.id.remind_vibrate);
		course_remind = (TextView) view.findViewById(R.id.course_remind);
		share_course_to = (TextView) view.findViewById(R.id.share_course_to);
		user_mail = (TextView) view.findViewById(R.id.user_mail);
		tools = (TextView) view.findViewById(R.id.tools);
		calendar = (TextView) view.findViewById(R.id.calendar);
		course = (TextView) view.findViewById(R.id.course);
		remindtime = (TextView) view.findViewById(R.id.remindtime);
		message = (TextView) view.findViewById(R.id.message);
		remindtime_seekbar = (SeekBar) view
				.findViewById(R.id.remindtime_seekbar);
		video_recoder_btn = (Button) view.findViewById(R.id.video_recorder);
		timer = (Button) view.findViewById(R.id.timer);
		photo = (Button) view.findViewById(R.id.photo);
		audio_record = (Button) view.findViewById(R.id.audio_record);
		tools_all = (LinearLayout) view.findViewById(R.id.tools_all);

		// 消息盒子 需要权限判断
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user_mail.getText().equals("请登陆")) {
					Toast.makeText(getActivity(), "请登录后再查看消息", 2).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
				startActivity(new Intent(getActivity(), MessageActivity.class));
			}
		});

		// 日程备份
		calendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user_mail.getText().equals("请登陆")) {
					Toast.makeText(getActivity(), "请登录后再进行备份", 2).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));

				} else {
					startActivity(new Intent(getActivity(),
							SaveCalendarActivity.class));
				}
			}
		});
		// 课表备份
		course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user_mail.getText().equals("请登陆")) {
					Toast.makeText(getActivity(), "请登录后再进行备份", 2).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));

				} else {
					startActivity(new Intent(getActivity(),
							SaveCourseActivity.class));
				}
			}
		});
		course_remind_all = (LinearLayout) view
				.findViewById(R.id.course_remind_all);

		remindtime_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						remindtime.setText("提前" + seekBar.getProgress() + "分");
						if (seekBar.getProgress() > 0) {
							clock_time = seekBar.getProgress();
							remind_voice.setImageDrawable(getResources()
									.getDrawable(R.drawable.setting_off));
							editor_voice = sp_voice.edit();
							editor_voice.putBoolean("check_voice", false);
							editor_voice.commit();
							check_clock = false;

							// 关闭闹铃
							Intent intent = new Intent(getActivity(),
									AlarmReceiver.class);
							PendingIntent sender = PendingIntent.getBroadcast(
									getActivity(), 0, intent, 0);
							// 取消闹铃
							AlarmManager am = (AlarmManager) getActivity()
									.getSystemService(Context.ALARM_SERVICE);
							am.cancel(sender);

							// 关闭震动
							remind_vibrate.setImageDrawable(getResources()
									.getDrawable(R.drawable.setting_off));
							editor_vibrate = sp_vibrate.edit();
							editor_vibrate.putBoolean("check_vibrate", false);
							editor_vibrate.commit();
							check_vibrate = false;

							Toast.makeText(getActivity(), "请打开响铃和震动提醒",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						remindtime.setText("提前" + seekBar.getProgress() + "分");

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						remindtime.setText("提前" + seekBar.getProgress() + "分");
					}
				});

		course_remind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// VISIBLE = 0x00000000;
				// INVISIBLE = 0x00000004;
				// GONE = 0x00000008;
				if (course_remind_all.getVisibility() == 0) {

					course_remind_all.setVisibility(View.GONE);
				} else {

					course_remind_all.setVisibility(View.VISIBLE);
				}
			}
		});
		// 震动是否打开
		sp_vibrate = getActivity().getSharedPreferences(
				"course_remind_vibrate", Context.MODE_PRIVATE);
		editor_vibrate = sp_vibrate.edit();
		check_vibrate = sp_vibrate.getBoolean("check_vibrate", false);
		if (!check_vibrate) {
			remind_vibrate.setImageDrawable(getResources().getDrawable(
					R.drawable.setting_off));
		} else {
			remind_vibrate.setImageDrawable(getResources().getDrawable(
					R.drawable.setting_on));
		}
		view.findViewById(R.id.vibrate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!check_vibrate) {
							remind_vibrate.setImageDrawable(getResources()
									.getDrawable(R.drawable.setting_on));
							editor_vibrate = sp_vibrate.edit();
							editor_vibrate.putBoolean("check_vibrate", true);
							editor_vibrate.putBoolean("check_vibrate_onOroff",
									true);
							editor_vibrate.commit();
							check_vibrate = true;
							check_onOroff = true;
						} else {
							remind_vibrate.setImageDrawable(getResources()
									.getDrawable(R.drawable.setting_off));
							editor_vibrate = sp_vibrate.edit();
							editor_vibrate.putBoolean("check_vibrate", false);
							editor_vibrate.putBoolean("check_vibrate_onOroff",
									false);
							editor_vibrate.commit();
							check_vibrate = false;
							check_onOroff = false;

						}
					}
				});
		// 响铃提醒是否打开
		sp_voice = getActivity().getSharedPreferences("course_remind_clock",
				Context.MODE_PRIVATE);
		editor_voice = sp_voice.edit();
		check_clock = sp_voice.getBoolean("check_voice", false);
		if (!check_clock) {
			remind_voice.setImageDrawable(getResources().getDrawable(
					R.drawable.setting_off));
		} else {
			remind_voice.setImageDrawable(getResources().getDrawable(
					R.drawable.setting_on));
		}
		view.findViewById(R.id.voice).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!check_clock) {
					remind_voice.setImageDrawable(getResources().getDrawable(
							R.drawable.setting_on));
					editor_voice = sp_voice.edit();
					editor_voice.putBoolean("check_voice", true);
					editor_voice.commit();
					check_clock = true;

					// 设置闹铃 (08:00)
					Intent intent = new Intent(getActivity(),
							AlarmReceiver.class);
					PendingIntent sender = PendingIntent.getBroadcast(
							getActivity(), 0, intent, 0);

					long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
					long systemTime = System.currentTimeMillis();

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
					if (clock_time == 60) {
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.HOUR_OF_DAY, 7);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
					} else {
						calendar.set(Calendar.MINUTE, 60 - clock_time);
						calendar.set(Calendar.HOUR_OF_DAY, 7);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
					}
					// 选择的每天定时时间
					long selectTime = calendar.getTimeInMillis();

					// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
					if (systemTime > selectTime) {
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						selectTime = calendar.getTimeInMillis();
					}

					// 计算现在时间到设定时间的时间差
					long time = selectTime - systemTime;
					firstTime += time;

					// 进行闹铃注册
					AlarmManager manager = (AlarmManager) getActivity()
							.getSystemService(Context.ALARM_SERVICE);
					manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
							firstTime, 24 * 60 * 60 * 1000, sender);

					// 设置闹铃 (14:00)
					Intent intent2 = new Intent(getActivity(),
							AlarmReceiver.class);
					PendingIntent sender2 = PendingIntent.getBroadcast(
							getActivity(), 0, intent2, 0);

					long firstTime2 = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
					long systemTime2 = System.currentTimeMillis();

					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTimeInMillis(System.currentTimeMillis());
					calendar2.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
					if (clock_time == 60) {
						calendar2.set(Calendar.MINUTE, 0);
						calendar2.set(Calendar.HOUR_OF_DAY, 8);
						calendar2.set(Calendar.SECOND, 0);
						calendar2.set(Calendar.MILLISECOND, 0);
					} else {
						calendar2.set(Calendar.MINUTE, 60 - clock_time);
						calendar2.set(Calendar.HOUR_OF_DAY, 14);
						calendar2.set(Calendar.SECOND, 0);
						calendar2.set(Calendar.MILLISECOND, 0);
					}
					// 选择的每天定时时间
					long selectTime2 = calendar2.getTimeInMillis();

					// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
					if (systemTime2 > selectTime2) {
						calendar2.add(Calendar.DAY_OF_MONTH, 1);
						selectTime2 = calendar2.getTimeInMillis();
					}

					// 计算现在时间到设定时间的时间差
					long time2 = selectTime2 - systemTime2;
					firstTime2 += time2;

					getActivity();
					// 进行闹铃注册
					AlarmManager manager2 = (AlarmManager) getActivity()
							.getSystemService(Context.ALARM_SERVICE);
					manager2.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
							firstTime2, 24 * 60 * 60 * 1000, sender2);

					Toast.makeText(getActivity(), "闹铃已打开", Toast.LENGTH_SHORT)
							.show();

					/**
					 * Intent intent = new Intent(getActivity(),
					 * AlarmReceiver.class); PendingIntent sender =
					 * PendingIntent.getBroadcast( getActivity(), 0, intent, 0);
					 * 
					 * // 过10s 执行这个闹铃 Calendar calendar =
					 * Calendar.getInstance();
					 * calendar.setTimeInMillis(System.currentTimeMillis());
					 * calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
					 * calendar.add(Calendar.SECOND, 10);
					 * 
					 * // 进行闹铃注册 AlarmManager manager = (AlarmManager)
					 * getActivity() .getSystemService(Context.ALARM_SERVICE);
					 * manager.set(AlarmManager.RTC_WAKEUP,
					 * calendar.getTimeInMillis(), sender);
					 * 
					 * Toast.makeText(getActivity(), "设置简单闹铃成功!",
					 * Toast.LENGTH_LONG).show();
					 */
				} else if (check_clock) {
					remind_voice.setImageDrawable(getResources().getDrawable(
							R.drawable.setting_off));
					editor_voice = sp_voice.edit();
					editor_voice.putBoolean("check_voice", false);
					editor_voice.commit();
					check_clock = false;

					// 关闭闹铃
					Intent intent = new Intent(getActivity(),
							AlarmReceiver.class);
					PendingIntent sender = PendingIntent.getBroadcast(
							getActivity(), 0, intent, 0);
					// 取消闹铃
					AlarmManager am = (AlarmManager) getActivity()
							.getSystemService(Context.ALARM_SERVICE);
					am.cancel(sender);
					Toast.makeText(getActivity(), "闹铃已关闭", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		tools.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// VISIBLE = 0x00000000;
				// INVISIBLE = 0x00000004;
				// GONE = 0x00000008;
				if (tools_all.getVisibility() == 0) {

					tools_all.setVisibility(View.GONE);
				} else {

					tools_all.setVisibility(View.VISIBLE);
				}
			}
		});

		if (!getActivity()
				.getSharedPreferences("setting", Context.MODE_PRIVATE)
				.getString("user_mail", "").equals("")) {

			user_mail.setText(getActivity().getSharedPreferences("setting",
					Context.MODE_PRIVATE).getString("user_mail", "请登陆"));
			if (BitmapFactory.decodeFile("/mnt/sdcard/learnpark/user/user.png") != null) {
				user_photo.setImageBitmap(BitmapFactory
						.decodeFile("/mnt/sdcard/learnpark/user/user.png"));
			}
		}

		user_mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user_mail.getText().equals("请登陆")) {

					startActivity(new Intent(getActivity(), LoginActivity.class));

				} else {
					startActivity(new Intent(getActivity(),
							UserCenterActivity.class));
				}
			}
		});
		user_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user_mail.getText().equals("请登陆")) {

					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					startActivity(new Intent(getActivity(),
							UserCenterActivity.class));
				}
			}
		});

		video_recoder_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 开始录像
				startActivity(new Intent(getActivity(), VedioActivity.class));
			}
		});
		audio_record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 开始录音
				startActivity(new Intent(getActivity(), RecordActivity.class));
			}
		});

		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 拍照
				startActivity(new Intent(getActivity(), PhotoActivity.class));
			}
		});
		timer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 开始计时
				startActivity(new Intent(getActivity(), TimerActivity.class));
			}
		});
		// 分享应用
		share_course_to.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent shareIntent = new Intent();
				shareIntent.setType("text/plain");
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, getResources()
						.getString(R.string.share_info));
				startActivity(shareIntent);
			}
		});
		return view;
	}
}
