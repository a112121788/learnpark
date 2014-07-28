package net.learnpark.app.teacher.learnpark;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import net.learnpark.app.teacher.learnpark.fragment.FragmentA;
import net.learnpark.app.teacher.learnpark.fragment.FragmentB;
import net.learnpark.app.teacher.learnpark.fragment.FragmentC;
import net.learnpark.app.teacher.learnpark.fragment.FragmentViewPagerAdapter;
import net.learnpark.app.teacher.learnpark.net.ConnectState;
import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.CreateMenu;
import net.learnpark.app.teacher.learnpark.shixian.QiandaoMessages;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.ByteUtil;
import net.learnpark.app.teacher.learnpark.util.CourseUtil;
import net.learnpark.app.teacher.learnpark.util.Setting;
import net.tsz.afinal.FinalDb;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		FragmentB.OnCourseChangeListener {

	private String username = "";

	private ActionBar ab;// Actionbar
	private ViewPager vp; // ViewPager
	private Boolean IsConnected;// 当前网络连接状态

//	private String share_path = Environment.getExternalStorageDirectory()
//			.getPath() + "/learnpark/download/share.jpg";
	List<Fragment> fragments; // fragments list集合
	FragmentA faA;
	FragmentB fbB; // 课表
	FragmentC fcC;
	int currentid = 0; // 当前所在的fragment
	// MyPagerAdapter mypadapter; // 自定义的 PagerAdapter。 作用 Tab滑动
	FragmentViewPagerAdapter myviewpagerpadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// 设置退出
		SysApplication.getInstance().addActivity(this);
		// 获得一下当前的版本号
		String nowversionnum = Setting.getVersionName(MainActivity.this);
		// 判断软件是否是第一次运行
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"setting", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		editor.putString("nowversionnum", nowversionnum);
		editor.commit();
		if (isFirstRun) {
			CourseUtil.createCourses(MainActivity.this);
			editor.putBoolean("isFirstRun", false);
			editor.putString("user_mail", "");
			editor.putString("password", "");
			editor.putString("user_name", "");
			editor.putString("user_school", "");
			editor.putString("user_department", "");
			editor.putString("newversionnum", nowversionnum);
			editor.commit();
		} else {
			username = sharedPreferences.getString("user_mail", "");
		}

		// 查看当前程序的安装目录
		// Log.d("TAG",getApplicationContext().getFilesDir().getAbsolutePath()+"  "+88);

		// 对于有物理菜单键的手机设置sHasPermanentMenuKey为false
		Setting.getOverflowMenu(getApplicationContext());

		// 设置tab导航
		ab = getSupportActionBar();
		// 设置导航模式
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 创建导航
		ActionBar.Tab tab1 = ab.newTab();
		ActionBar.Tab tab2 = ab.newTab();
		ActionBar.Tab tab3 = ab.newTab();

		tab1.setCustomView(R.drawable.actionbar_tab_bg);
		TextView title1 = (TextView) tab1.getCustomView().findViewById(
				R.id.tab_title);
		title1.setText("我要点名");
		tab2.setCustomView(R.drawable.actionbar_tab_bg);
		TextView title2 = (TextView) tab2.getCustomView().findViewById(
				R.id.tab_title);
		title2.setText("我的课程");
		tab3.setCustomView(R.drawable.actionbar_tab_bg);
		TextView title3 = (TextView) tab3.getCustomView().findViewById(
				R.id.tab_title);
		title3.setText("我的班级");

		MyTabListener myTabListener = new MyTabListener();
		tab1.setTabListener(myTabListener);
		tab2.setTabListener(myTabListener);
		tab3.setTabListener(myTabListener);
		// 添加选项到ActionBar
		ab.addTab(tab1);
		ab.addTab(tab2);
		ab.addTab(tab3);
		// 初始化ViewPage
		vp = (ViewPager) findViewById(R.id.vp);
		fragments = new ArrayList<Fragment>();
		faA = new FragmentA();
		fbB = new FragmentB();
		fcC = new FragmentC();
		fragments.add(faA);
		fragments.add(fbB);
		fragments.add(fcC);
		// mypadapter = new MyPagerAdapter(getSupportFragmentManager(),
		// fragments);

		// 这里不用上面的adapter是因为，当切换到第三个页面时会清除掉第一二个页面，这样就又会调用oncreateview，会导致屏幕闪一下，体验不好
		myviewpagerpadapter = new FragmentViewPagerAdapter(
				getSupportFragmentManager(), vp, fragments);
		vp.setAdapter(myviewpagerpadapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				ab.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		// 调用CreateMenu函数进行菜单的创建
		new CreateMenu(menu);
		// 设置分享按钮的功能
		MenuItem item = menu.findItem(4);
		ShareActionProvider sap = new ShareActionProvider(this);
		MenuItemCompat.setActionProvider(item, sap);
		Intent shareIntent = new Intent();
		shareIntent.setType("text/plain");
		shareIntent.setAction(Intent.ACTION_SEND);
		// shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(share_path));
		shareIntent.putExtra(Intent.EXTRA_TEXT, "亲，你也来使用学苑吧");
		sap.setShareIntent(shareIntent);
		MenuItemCompat.setActionProvider(item, sap);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent1 = new Intent(this, UserActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("uid", MainActivity.this.toString());
			intent1.putExtras(bundle1);
			currentid = vp.getCurrentItem();
			startActivityForResult(intent1, 1);
			return true;

		case 2:
			// 判断当前网络状态
			IsConnected = ConnectState.isNetworkConnected(this);
			if (IsConnected == true) {
				if (username != null && username.length() > 0) {
					UpLoadQiandaoMessage();
				} else {
					Toast.makeText(MainActivity.this, "请先登录帐号",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(MainActivity.this, "没有网络连接，无法上传",
						Toast.LENGTH_SHORT).show();
			}
			return true;

		case 3:
			Intent intent10 = new Intent(this, SendMessagesActivity.class);
			startActivity(intent10);
			return true;

		case 5:
			Intent intent3 = new Intent(this, SettingActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("uid", MainActivity.this.toString());
			intent3.putExtras(bundle3);
			currentid = vp.getCurrentItem();
			startActivityForResult(intent3, 3);
			return true;

		case 6:
			Intent intent5 = new Intent(this, FeedbackActivity.class);
			startActivity(intent5);
			return true;

		case 7:
			vp.setCurrentItem(1);
			// 获得当前时间信息
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);// 当前星期几
			if (day == 1) {
				day = 7;
			} else {
				day = day - 1;
			}
			fbB.AlertDialog_addcurses(day - 1, 0);
			return true;
		case 8:
			vp.setCurrentItem(2);
			fcC.AlertDialog_addclasses();
			return true;
		case 9:
			Intent intent8 = new Intent(this, AddStudentsActivity.class);
			Bundle bundle8 = new Bundle();
			bundle8.putString("uid", MainActivity.this.toString());
			intent8.putExtras(bundle8);
			currentid = vp.getCurrentItem();
			startActivityForResult(intent8, 8);
			return true;
		case 10:
			Intent intent9 = new Intent(this, ImportStudentsActivity.class);
			Bundle bundle9 = new Bundle();
			bundle9.putString("uid", MainActivity.this.toString());
			intent9.putExtras(bundle9);
			currentid = vp.getCurrentItem();
			startActivityForResult(intent9, 9);
			return true;
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog alertDialog;
			AlertDialog.Builder bulider = new Builder(MainActivity.this);
			bulider.setIcon(R.drawable.ic_launcher);
			bulider.setTitle("确定退出？");
			// 添加一个确定添加按钮
			bulider.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							SysApplication.getInstance().exit();
						}
					});

			// 添加一个取消按钮
			bulider.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog = bulider.create();
			alertDialog.show();
		}else if (keyCode == KeyEvent.KEYCODE_MENU) {
			Log.d("TAG","点击了物理菜单键");
		}

		return super.onKeyDown(keyCode, event);
	}

	// Tab滑动
	// private class MyPagerAdapter extends FragmentPagerAdapter {
	// private List<Fragment> fragments;
	//
	// public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	// super(fm);
	// this.fragments = fragments;
	// }
	//
	// @Override
	// public Fragment getItem(int postion) {
	// return fragments.get(postion);
	// }
	//
	// @Override
	// public int getCount() {
	// return fragments.size();
	// }
	// }

	// Tab监听
	class MyTabListener implements ActionBar.TabListener {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (vp != null) {
				vp.setCurrentItem(tab.getPosition());
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {

		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 3 && arg1 == RESULT_OK) {
			fbB = new FragmentB();
			fcC = new FragmentC();
			fragments.remove(1);
			fragments.remove(1);
			fragments.add(fbB);
			fragments.add(fcC);

			myviewpagerpadapter = new FragmentViewPagerAdapter(
					getSupportFragmentManager(), vp, fragments);
			vp.setAdapter(myviewpagerpadapter);
			vp.setCurrentItem(currentid);
		}

		if ((arg0 == 8 || arg0 == 9) && arg1 == RESULT_OK) {
			fcC = new FragmentC();
			fragments.remove(2);
			fragments.add(fcC);

			Log.d("TAG", "返回了" + "  " + 88);
			myviewpagerpadapter = new FragmentViewPagerAdapter(
					getSupportFragmentManager(), vp, fragments);
			vp.setAdapter(myviewpagerpadapter);
			vp.setCurrentItem(currentid);
		}

		if (arg0 == 1 && arg1 == RESULT_OK) {
			SharedPreferences sharedPreferences = this.getSharedPreferences(
					"setting", MODE_PRIVATE);
			username = sharedPreferences.getString("user_mail", "");
			faA.updateUserMessages();
		}

	}

	// 上传签到信息
	private void UpLoadQiandaoMessage() {
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
					HttpPost post = new HttpPost(
							NetCantants.UPLOAD_QIANDAOMESSAGES_URL);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
					list.add(new BasicNameValuePair("name", URLEncoder.encode(
							username, "utf-8")));
					// 将签到信息转换成json数据
					java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<QiandaoMessages>>() {
					}.getType();
					List<QiandaoMessages> qiandaolist = GetQiandaoList();
					String beanListToJson = new Gson()
							.toJson(qiandaolist, type);
					list.add(new BasicNameValuePair("qiandao", URLEncoder
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
					Toast.makeText(MainActivity.this, "上传成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "网络连接异常，请稍候重试",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute(NetCantants.UPLOAD_QIANDAOMESSAGES_URL);
	}

	// 获得签到列表的List集合
	private List<QiandaoMessages> GetQiandaoList() {
		FinalDb QiandaoDb = FinalDb.create(MainActivity.this);
		List<QiandaoMessages> qiandaoliat = QiandaoDb
				.findAll(QiandaoMessages.class);
		return qiandaoliat;
	}

	// fragment之间的通信
	@Override
	public void onCourseChanged(int position) {
		if (position == 0) {

			if (faA != null) {

				faA.updateCourseNum();
			} else {

				FragmentA newFragment = new FragmentA();

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				transaction.replace(faA.getId(), newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}
		} else if (position == 1) {

		}
	}

}
