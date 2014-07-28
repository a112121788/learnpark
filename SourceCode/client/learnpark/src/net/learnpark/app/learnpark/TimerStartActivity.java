package net.learnpark.app.learnpark;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.learnpark.app.learnpark.util.VibratorUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class TimerStartActivity extends ActionBarActivity {
	static int hour = -1;
	static int minute = -1;
	static int second = -1;
	final static String tag = "tag";
	TextView timeView;
	Timer timer;
	TimerTask timerTask;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handle!");
			if (hour == 0) {
				if (minute == 0) {
					if (second == 0) {// 时间到
						timeView.setText("时间到 !");
						VibratorUtil.Vibrate(TimerStartActivity.this, 2000);
						finish();
						if (timer != null) {
							timer.cancel();
							timer = null;
						}
						if (timerTask != null) {
							timerTask = null;
						}
					} else {// 00:30,00:08
						second--;
						if (second >= 10) {
							timeView.setText("0" + hour + ":" + "0" + minute
									+ ":" + second);
						} else {
							timeView.setText("0" + hour + ":" + "0" + minute
									+ ":0" + second);
						}
					}
				} else {// minute!=0,33:00->32:59
					if (second == 0) {
						second = 59;
						minute--;
						if (minute >= 10) {// 33:30
							timeView.setText("0" + hour + ":" + minute + ":"
									+ second);
						} else {// 06:30
							timeView.setText("0" + hour + ":" + "0" + minute
									+ ":" + second);
						}
					} else {// 32:30->32:29
						second--;
						if (second >= 10) {
							if (minute >= 10) {
								timeView.setText("0" + hour + ":" + minute
										+ ":" + second);
							} else {// 09:30
								timeView.setText("0" + hour + ":" + "0"
										+ minute + ":" + second);
							}
						} else {// 12:30
							if (minute >= 10) {
								timeView.setText("0" + hour + ":" + minute
										+ ":0" + second);
							} else {// 08:30
								timeView.setText("0" + hour + ":" + "0"
										+ minute + ":0" + second);
							}
						}
					}
				}
			}

			// hour>0
			else {
				if (minute == 0) {// hour>0,minute=0
					if (second == 0) {
						minute = 59;
						second = 59;
						hour--;
						if (hour >= 10) {
							if (second >= 10) {
								timeView.setText(hour + ":" + minute + ":"
										+ second);
							} else {
								timeView.setText(hour + ":" + minute + ":0"
										+ second);
							}
						} else {
							if (second >= 10) {
								timeView.setText("0" + hour + ":" + minute
										+ ":" + second);
							} else {
								timeView.setText("0" + hour + ":" + minute
										+ ":0" + second);
							}
						}
					} else {
						second--;
						if (hour >= 10) {
							if (second >= 10) {
								timeView.setText(hour + ":0" + minute + ":"
										+ second);
							} else {
								timeView.setText(hour + ":0" + minute + ":0"
										+ second);
							}
						} else {
							if (second >= 10) {
								timeView.setText("0" + hour + ":0" + minute
										+ ":" + second);
							} else {
								timeView.setText("0" + hour + ":0" + minute
										+ ":0" + second);
							}
						}
					}
				} else {// hour>0,minute!=0
					if (second == 0) {
						second = 59;
						minute--;
						if (hour >= 10) {
							if (second >= 10) {
								if (minute >= 10) {
									timeView.setText(hour + ":" + minute + ":"
											+ second);
								} else {
									timeView.setText(hour + ":0" + minute + ":"
											+ second);
								}
							} else {
								if (minute >= 10) {
									timeView.setText(hour + ":" + minute + ":0"
											+ second);
								} else {
									timeView.setText(hour + ":0" + minute
											+ ":0" + second);
								}
							}
						} else {
							if (second >= 10) {
								if (minute >= 10) {
									timeView.setText("0" + hour + ":" + minute
											+ ":" + second);
								} else {
									timeView.setText("0" + hour + ":0" + minute
											+ ":" + second);
								}
							} else {
								if (minute >= 10) {
									timeView.setText("0" + hour + ":" + minute
											+ ":0" + second);
								} else {
									timeView.setText("0" + hour + ":0" + minute
											+ ":0" + second);
								}
							}
						}

					} else {
						second--;
						if (hour >= 10) {
							if (second >= 10) {
								if (minute >= 10) {
									timeView.setText(hour + ":" + minute + ":"
											+ second);
								} else {
									timeView.setText(hour + ":0" + minute + ":"
											+ second);
								}
							} else {
								if (minute >= 10) {
									timeView.setText(hour + ":" + minute + ":0"
											+ second);
								} else {
									timeView.setText(hour + ":0" + minute
											+ ":0" + second);
								}
							}
						} else {
							if (second >= 10) {
								if (minute >= 10) {
									timeView.setText("0" + hour + ":" + minute
											+ ":" + second);
								} else {
									timeView.setText("0" + hour + ":0" + minute
											+ ":" + second);
								}
							} else {
								if (minute >= 10) {
									timeView.setText("0" + hour + ":" + minute
											+ ":0" + second);
								} else {
									timeView.setText("0" + hour + ":0" + minute
											+ ":0" + second);
								}
							}
						}

					}
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timerstart);
		getSupportActionBar().hide();
		timeView = (TextView) findViewById(R.id.myTime);
		SysApplication.getInstance().addActivity(this); 
		if (hour == -1 && minute == -1 && second == -1) {
			Intent intent = getIntent();
			ArrayList<Integer> times = intent.getIntegerArrayListExtra("times");
			// minute = times.get(0);
			// second = times.get(1);
			hour = times.get(0);
			minute = times.get(1);
			second = times.get(2);
		}

		timeView.setText(hour + ":" + minute + ":" + second);

		timerTask = new TimerTask() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);

	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		hour = -1;
		minute = -1;
		second = -1;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(), "没到点呢，急什么！", 2).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_HOME == keyCode) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}
}
