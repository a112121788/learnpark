package net.learnpark.app.learnpark;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TimerActivity extends ActionBarActivity {
	TextView minute_time;
	TextView second_time;
	Button startButton;// 开始计时
	SeekBar minute_seekbar;
	SeekBar second_seekbar;
	int minute = 5;
	int second=5;
	int hour;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		
		minute_time = (TextView) findViewById(R.id.minute_time);
		second_time = (TextView) findViewById(R.id.second_time);
		startButton = (Button) findViewById(R.id.button_start);
		minute_seekbar = (SeekBar) findViewById(R.id.minute_seekbar);
		second_seekbar = (SeekBar) findViewById(R.id.second_seekbar);
		minute_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO 自动生成的方法存根
						minute = seekBar.getProgress();

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						minute = seekBar.getProgress();
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						minute = seekBar.getProgress();
						minute_time.setText(progress + " 分");
					}
				});
		second_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO 自动生成的方法存根
						second = seekBar.getProgress();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						second = seekBar.getProgress();
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						second = seekBar.getProgress();
						second_time.setText(progress + " 秒");
					}
				});

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hour != 0 || minute != 0 || second != 0) {
					System.out.println(hour + ":" + minute + ":" + second);
					ArrayList<Integer> list = new ArrayList<Integer>();
					list.add(hour);
					list.add(minute);
					list.add(second);
					Intent intent = new Intent(getApplicationContext(),
							TimerStartActivity.class);
					intent.putIntegerArrayListExtra("times", list);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}