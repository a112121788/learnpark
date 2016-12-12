package net.learnpark.app.teacher.learnpark;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.learnpark.app.teacher.learnpark.net.ConnectState;
import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.ByteUtil;
import net.learnpark.app.teacher.learnpark.util.ClassesUtil;
import net.learnpark.app.teacher.learnpark.util.StudentsUtil;
import net.tsz.afinal.FinalDb;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ImportStudentsActivity extends ActionBarActivity implements
		OnClickListener {

	private String username;
	private int INSERTOK = 1;
	private int CONNECTFAIL = 0;
	private Boolean IsConnected;

	// 设置下拉刷新
	private PullToRefreshListView mPullRefreshListView;

	private ListView lv;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;

	List<Classes> ClassesList = null;
	List<Students> StudentsList = null;

	FinalDb classesDb;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == INSERTOK) {
				Toast.makeText(ImportStudentsActivity.this, "导入成功",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == CONNECTFAIL) {
				Toast.makeText(ImportStudentsActivity.this, "获取列表失败，请稍候再试",
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_importstudents);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("user_mail", "");

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView_addclass);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 判断当前网络状态
						IsConnected = ConnectState
								.isNetworkConnected(ImportStudentsActivity.this);
						if (IsConnected == true) {
							if (username != null && username.length() > 0) {
								String label = DateUtils.formatDateTime(
										getApplicationContext(),
										System.currentTimeMillis(),
										DateUtils.FORMAT_SHOW_TIME
												| DateUtils.FORMAT_SHOW_DATE
												| DateUtils.FORMAT_ABBREV_ALL);

								// Update the LastUpdatedLabel
								refreshView.getLoadingLayoutProxy()
										.setLastUpdatedLabel(label);

								// Do work to refresh the list here.
								new GetDataTask().execute();

							} else {
								Toast.makeText(ImportStudentsActivity.this,
										"请先登陆帐号", Toast.LENGTH_SHORT).show();
							}

						} else {
							Toast.makeText(ImportStudentsActivity.this,
									"当前无网络连接，获取列表失败", Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

		// lv = (ListView) findViewById(R.id.listView_addclass);
		data = new ArrayList<Map<String, String>>();

		adapter = new SimpleAdapter(this, data,
				R.layout.item_listview_addstudents, new String[] {
						"coursename", "classname", "department" }, new int[] {
						R.id.item_addclass_coursename1,
						R.id.item_addclass_classname1,
						R.id.item_addclass_department1, });

		lv = mPullRefreshListView.getRefreshableView();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String classname = data.get(arg2 - 1).get("classname");
				String coursename = data.get(arg2 - 1).get("coursename");
				String department = data.get(arg2 - 1).get("department");
				Classes classes = new Classes();
				classes.setClassname(classname);
				classes.setCoursename(coursename);
				classes.setDepartment(department);
				Boolean bl = ClassesUtil.insertClasses(
						ImportStudentsActivity.this, classes);
				if (bl == false) {
					Toast.makeText(ImportStudentsActivity.this, "添加班级失败",
							Toast.LENGTH_SHORT).show();
				}
				Log.d("TAG", "" + classname + "  data.size()" + classname);
				GetStudentsListPost(classname);
			}
		});

		// 判断当前网络状态
		IsConnected = ConnectState.isNetworkConnected(this);
		if (IsConnected == true) {
			if (username != null && username.length() > 0) {
				GetClassListPost();
			} else {
				Toast.makeText(ImportStudentsActivity.this, "请先登陆帐号",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(ImportStudentsActivity.this, "当前无网络连接，获取列表失败",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void GetClassListPost() {
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
				// 请求超时
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				// 读取超时
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 5000);
				try {
					HttpPost post = new HttpPost(
							NetCantants.GET_CLASSES_LIST_URL);

					List<NameValuePair> list = new ArrayList<NameValuePair>();
					// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
					list.add(new BasicNameValuePair("name", URLEncoder.encode(
							username, "utf-8")));
					// list.add(new BasicNameValuePair("data",data));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
					post.setEntity(entity);// 设置发送的数据
					HttpResponse response = client.execute(post);// 发送请求到服务器
					if (response.getStatusLine().getStatusCode() == 200) {
						byte[] respData = ByteUtil.inputStream2Byte(response);
						// Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
						// data.length);
						Log.d("TAG", ""
								+ response.getStatusLine().getStatusCode()
								+ "  状态码" + 7);
						return new String(respData);
					} else {
						Message msg = new Message();
						// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
						msg.what = CONNECTFAIL;
						handler.sendMessage(msg);
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
				if (result != null) {
					if (result.equals("NOCLASSESLIST")) {
						Toast.makeText(ImportStudentsActivity.this,
								"亲，服务器端没有学生信息", Toast.LENGTH_SHORT).show();
					} else {
						// 更新ui,result从服务器上返回的json数据
						Gson g = new Gson();
						Type typeOfT = new TypeToken<List<Classes>>() {
						}.getType();
						Log.d("TAG", "" + result + "  data.size()" + 7);
						ClassesList = g.fromJson(result, typeOfT);
						for (Classes class1 : ClassesList) {
							Map<String, String> m1 = new HashMap<String, String>();
							m1.put("coursename", class1.getCoursename());
							m1.put("classname", class1.getClassname());
							m1.put("department", class1.getDepartment());
							data.add(m1);
						}
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(ImportStudentsActivity.this, "服务器连接异常",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute(NetCantants.GET_CLASSES_LIST_URL);
	}

	private void GetStudentsListPost(final String classname1) {

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
				// 请求超时
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
				// 读取超时
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 8000);
				try {
					HttpPost post = new HttpPost(
							NetCantants.GET_STUDENTS_LIST_URL);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
					list.add(new BasicNameValuePair("name", URLEncoder.encode(
							username, "utf-8")));
					list.add(new BasicNameValuePair("classname", URLEncoder
							.encode(classname1, "utf-8")));
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
						Message msg = new Message();
						// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
						msg.what = CONNECTFAIL;
						handler.sendMessage(msg);
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
				if (result != null) {
					if (result.equals("NOSTUDENTSLIST")) {
						Toast.makeText(ImportStudentsActivity.this,
								"亲，服务器端没有学生信息", Toast.LENGTH_SHORT).show();
					} else {
						// 更新ui,result从服务器上返回的json数据
						Gson g = new Gson();
						Type typeOfT = new TypeToken<List<Students>>() {
						}.getType();
						Log.d("TAG", "" + result + "  data.size()" + 7);
						StudentsList = g.fromJson(result, typeOfT);
						setResult(RESULT_OK, (new Intent()).setAction(9 + ""));
						StudentsUtil.insertStudents(
								ImportStudentsActivity.this, StudentsList);
						Message msg = new Message();
						// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
						msg.what = INSERTOK;
						handler.sendMessage(msg);
						Log.d("TAG", "" + StudentsList.size() + "  data.size()"
								+ 7);
					}
				} else {
					Toast.makeText(ImportStudentsActivity.this, "服务器连接异常",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute(NetCantants.GET_STUDENTS_LIST_URL);
	}

	// 设置下拉刷新的
	private class GetDataTask extends AsyncTask<Void, Void, String> {
		// 后台处理部分
		@Override
		protected String doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			// 发送post请求到服务器
			HttpClient client = new DefaultHttpClient();
			// 请求超时
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					5000);
			try {
				HttpPost post = new HttpPost(NetCantants.GET_CLASSES_LIST_URL);
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				// 添加参数，必须编码，否则服务器乱码，服务器必须使用urldecoder解码
				list.add(new BasicNameValuePair("name", URLEncoder.encode(
						username, "utf-8")));
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
					Message msg = new Message();
					// Log.d("TAG", thisname + "     " + Num + "  " + 5588);
					msg.what = CONNECTFAIL;
					handler.sendMessage(msg);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String result) {

			if (result != null) {
				// 更新ui,result从服务器上返回的json数据

				// 删除原来的数据，显示新的数据
				data = new ArrayList<Map<String, String>>();

				Gson g = new Gson();
				Type typeOfT = new TypeToken<List<Classes>>() {
				}.getType();
				// Log.d("TAG", "" + result + "  下拉了" + 7);
				ClassesList = g.fromJson(result, typeOfT);
				for (Classes class1 : ClassesList) {
					Map<String, String> m1 = new HashMap<String, String>();
					m1.put("department", class1.getDepartment());
					m1.put("classname", class1.getClassname());
					m1.put("coursename", class1.getCoursename());
					data.add(m1);
				}

				adapter = new SimpleAdapter(ImportStudentsActivity.this, data,
						R.layout.item_listview_addstudents, new String[] {
								"coursename", "classname", "department" },
						new int[] { R.id.item_addclass_coursename1,
								R.id.item_addclass_classname1,
								R.id.item_addclass_department1, });
				lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
			} else {
				Toast.makeText(ImportStudentsActivity.this, "亲，服务器端没有学生信息",
						Toast.LENGTH_SHORT).show();
				mPullRefreshListView.onRefreshComplete();
			}
			super.onPostExecute(result);
		}
	}
}
