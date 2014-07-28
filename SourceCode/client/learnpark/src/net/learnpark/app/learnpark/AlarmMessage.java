package net.learnpark.app.learnpark;

import net.learnpark.app.learnpark.util.VibratorUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class AlarmMessage extends ActionBarActivity {
	MediaPlayer alarmMusic;
	SharedPreferences preferences;
	boolean check_vibrate_onOroff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent it = super.getIntent();
		SysApplication.getInstance().addActivity(this); 
		preferences = getSharedPreferences("course_remind_vibrate",
				Context.MODE_PRIVATE);
		check_vibrate_onOroff = preferences.getBoolean("check_vibrate_onOroff",
				false);
		// 震动
		if (check_vibrate_onOroff) {
			VibratorUtil.Vibrate(AlarmMessage.this, 15 * 1000);
		}
		// 播放音乐
		alarmMusic = MediaPlayer.create(this, R.raw.clock);
		alarmMusic.setLooping(true);
		alarmMusic.start();
		// 弹框提醒
		new AlertDialog.Builder(this)
				.setTitle("学苑提醒")
				.setMessage("上课时间快要到了!!!")
				.setPositiveButton("我知道了",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AlarmMessage.this.finish();
								alarmMusic.stop();
							}
						}).show();
	}
}
