package net.learnpark.app.teacher.learnpark.fragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import net.learnpark.app.teacher.learnpark.R;
import net.learnpark.app.teacher.learnpark.StudentsActivity;
import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.util.ClassesUtil;
import net.learnpark.app.teacher.learnpark.util.StudentsUtil;
import net.learnpark.app.teacher.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentC extends Fragment implements OnClickListener {

	FinalDb classDb;

	List<String> coursename_sum = new ArrayList<String>();
	List<Classes> classes_list;

	EditText add_coursename;// 课程名
	EditText add_classname; // 班级名称
	EditText add_department; // 所属院系

	// 设置下拉刷新
	private PullToRefreshListView mPullRefreshListView;

	private ListView lv;
	private List<Map<String, String>> data;
	private SimpleAdapter adapter;
	private View view;

	int week;
	Calendar c = Calendar.getInstance();

	int mHour = 0;
	// int mHour=getmHour(c.get(Calendar.HOUR_OF_DAY));
	int mMinute = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab3, container, false);
		data = new ArrayList<Map<String, String>>();

		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.listView_class);

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(getActivity(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});

		classes_list = ClassesUtil.getClassesList(getActivity());

		if (classes_list != null && classes_list.size() > 0) {
			for (int i = 0; i < classes_list.size(); i++) {
				Map<String, String> m1 = new HashMap<String, String>();
				m1.put("classname", classes_list.get(i).getClassname());
				m1.put("department", classes_list.get(i).getDepartment());
				m1.put("coursename", classes_list.get(i).getCoursename());
				m1.put("stusum",
						StudentsUtil.getStudentsSum(getActivity(), classes_list
								.get(i).getClassname())
								+ "");
				data.add(m1);

			}
		}

		// 获取数据库中所有班级的信息
		// classDb = FinalDb.create(getActivity());
		// List<Classes> list_classes = classDb.findAll(Classes.class);
		// if (list_classes != null && list_classes.size() > 0) {
		// for (int i = 0; i < list_classes.size(); i++) {
		// if (data != null && data.size() > 0) {
		// // Log.d("TAG", "classes" + data.size() + "  data.size()" +
		// // 7);
		// for (int j = 0; j < data.size(); j++) {
		// if (data.get(j).get("classname")
		// .equals(list_classes.get(i).getClassname())
		// && data.get(j)
		// .get("coursename")
		// .equals(list_classes.get(i)
		// .getCoursename())) {
		// if (list_classes.get(i).getStunum() != null) {
		// Map<String, String> map = new HashMap<String, String>();
		// map = data.get(j);
		// map.put("stusum",
		// Integer.valueOf(data.get(j).get(
		// "stusum"))
		// + 1 + "");
		// data.set(j, map);
		// break;
		// }
		//
		// } else if (j == data.size() - 1) {
		// Map<String, String> m1 = new HashMap<String, String>();
		// m1.put("classname", list_classes.get(i)
		// .getClassname());
		// m1.put("coursename", list_classes.get(i)
		// .getCoursename());
		// if (list_classes.get(i).getStunum() == null) {
		// m1.put("stusum", 0 + "");
		// } else {
		// m1.put("stusum", 1 + "");
		// }
		// data.add(m1);
		// break;
		// }
		// }
		// } else {
		// Map<String, String> m1 = new HashMap<String, String>();
		// m1.put("classname", list_classes.get(i).getClassname());
		// m1.put("coursename", list_classes.get(i).getCoursename());
		// if (list_classes.get(i).getStunum() == null) {
		// m1.put("stusum", 0 + "");
		// } else {
		// m1.put("stusum", 1 + "");
		// }
		// data.add(m1);
		// }
		// }
		// }

		adapter = new SimpleAdapter(getActivity(), data,
				R.layout.item_listview_classes, new String[] { "classname",
						"coursename", "stusum", "department" }, new int[] {
						R.id.item_listview_classname1,
						R.id.item_listview_coursename1,
						R.id.item_listview_classstudentsum1,
						R.id.item_listview_department1 });

		lv = mPullRefreshListView.getRefreshableView();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 点击listview不同条目，进入相应的班级信息
				String classname = data.get(arg2 - 1).get("classname");
				String coursename = data.get(arg2 - 1).get("coursename");

				Intent intent = new Intent(getActivity(),
						StudentsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", getActivity().toString());
				intent.putExtras(bundle);
				intent.putExtra("classname", classname);
				intent.putExtra("coursename", coursename);
				startActivityForResult(intent, 10);
			}

		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String classname = data.get(arg2 - 1).get("classname");
				String coursename = data.get(arg2 - 1).get("coursename");
				removeClass(classname, coursename);
				return false;
			}

		});

		return view;
	}

	// 重写对话框点击监听事件
	@SuppressLint("HandlerLeak")
	class ButtonHandler extends Handler {
		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;
			}
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// if (requestCode == 10 && resultCode == getActivity().RESULT_OK) {
		// }

	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {
		// 后台处理部分
		@Override
		protected String doInBackground(Void... params) {

			data = new ArrayList<Map<String, String>>();

			classes_list = ClassesUtil.getClassesList(getActivity());

			if (classes_list != null && classes_list.size() > 0) {
				for (int i = 0; i < classes_list.size(); i++) {
					Map<String, String> m1 = new HashMap<String, String>();
					m1.put("classname", classes_list.get(i).getClassname());
					m1.put("department", classes_list.get(i).getDepartment());
					m1.put("coursename", classes_list.get(i).getCoursename());
					m1.put("stusum",
							StudentsUtil.getStudentsSum(getActivity(),
									classes_list.get(i).getClassname()) + "");
					data.add(m1);

				}
			}

			return null;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String result) {
			adapter = new SimpleAdapter(getActivity(), data,
					R.layout.item_listview_classes, new String[] { "classname",
							"coursename", "stusum", "department" }, new int[] {
							R.id.item_listview_classname1,
							R.id.item_listview_coursename1,
							R.id.item_listview_classstudentsum1,
							R.id.item_listview_department1 });
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);// 这句是必有的，AsyncTask规定的格式
		}
	}

	public void AlertDialog_addclasses() {

		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(getActivity());
		bulider.setIcon(R.drawable.ic_launcher);

		View layout = (View) LayoutInflater.from(getActivity()).inflate(
				R.layout.add_class, null);
		bulider.setView(layout);
		bulider.setTitle("添加班级");
		// 找到添加课程的控件
		add_classname = (EditText) layout.findViewById(R.id.add_classname);
		add_coursename = (EditText) layout.findViewById(R.id.add_coursename);
		add_department = (EditText) layout.findViewById(R.id.add_department);

		// 添加一个确定添加按钮
		bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (add_classname.getText().toString().equals("")
						|| add_coursename.getText().toString().equals("")
						|| add_department.getText().toString().equals("")) {
					VibratorUtil.Vibrate(getActivity(), 200);
					Toast.makeText(getActivity(), "请填写完整的信息", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (data != null && data.size() > 0) {

						for (int i = 0; i < data.size(); i++) {
							if (data.get(i).get("classname")
									.equals(add_classname.getText().toString())
									&& data.get(i)
											.get("coursename")
											.equals(add_coursename.getText()
													.toString())) {
								VibratorUtil.Vibrate(getActivity(), 200);
								Toast.makeText(getActivity(), "您所添加的课程已经添加过了",
										Toast.LENGTH_SHORT).show();
								break;
							} else if (i == data.size() - 1) {
								dialog.dismiss();
								classDb = FinalDb.create(getActivity());
								Classes classes = new Classes();
								classes.setClassname(add_classname.getText()
										.toString());
								classes.setCoursename(add_coursename.getText()
										.toString());
								classes.setDepartment(add_department.getText()
										.toString());
								classDb.save(classes);
								Map<String, String> m1 = new HashMap<String, String>();
								m1.put("classname", add_classname.getText()
										.toString());
								m1.put("coursename", add_coursename.getText()
										.toString());
								m1.put("stusum",
										StudentsUtil.getStudentsSum(
												getActivity(), add_classname
														.getText().toString())
												+ "");
								m1.put("department", add_department.getText()
										.toString());
								data.add(m1);
								adapter.notifyDataSetInvalidated();
								break;
							}
						}
					} else {
						dialog.dismiss();
						classDb = FinalDb.create(getActivity());
						Classes classes = new Classes();
						classes.setClassname(add_classname.getText().toString());
						classes.setCoursename(add_coursename.getText()
								.toString());
						classes.setDepartment(add_department.getText()
								.toString());
						classDb.save(classes);
						Map<String, String> m1 = new HashMap<String, String>();
						m1.put("classname", add_classname.getText().toString());
						m1.put("coursename", add_coursename.getText()
								.toString());
						m1.put("stusum",
								StudentsUtil.getStudentsSum(getActivity(),
										add_classname.getText().toString())
										+ "");
						m1.put("department", add_department.getText()
								.toString());
						data.add(m1);
						adapter.notifyDataSetInvalidated();
					}
				}

			}
		});

		// 添加一个取消按钮
		bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog = bulider.create();

		// 设置点击对话框的确定按钮后对话框不消失
		try {
			Field field = alertDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(alertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, new ButtonHandler(alertDialog));
		} catch (Exception e) {
		}
		alertDialog.show();
	}

	private void removeClass(final String classname,final String coursename) {
		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(getActivity());
		bulider.setIcon(R.drawable.ic_launcher);

		View layout = (View) LayoutInflater.from(getActivity()).inflate(
				R.layout.modify_classes, null);
		bulider.setView(layout);
		bulider.setTitle("删除班级");
		// 找到添加课程的控件
		TextView tv_classname=(TextView)layout.findViewById(R.id.edittext_modify_classname);
		TextView tv_coursename=(TextView)layout.findViewById(R.id.edittext_modify_coursename);

		tv_classname.setText(classname);
		tv_coursename.setText(coursename);
		// 添加一个确定添加按钮
		bulider.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Classes classes=new Classes();
				classes.setClassname(classname);
				classes.setCoursename(coursename);
				ClassesUtil.deleteClasses(getActivity(), classes);
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).get("classname").equals(classname)
							&&data.get(i).get("coursename").equals(coursename)) {
						data.remove(i);
						break;
					}
				}
				adapter.notifyDataSetInvalidated();
				dialog.dismiss();
			}
		});

		// 添加一个取消按钮
		bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog = bulider.create();

		// 设置点击对话框的确定按钮后对话框不消失
		try {
			Field field = alertDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(alertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, new ButtonHandler(alertDialog));
		} catch (Exception e) {
		}
		alertDialog.show();
	}

}
