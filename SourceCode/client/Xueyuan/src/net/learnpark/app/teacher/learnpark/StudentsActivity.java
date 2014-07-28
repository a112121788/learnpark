package net.learnpark.app.teacher.learnpark;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.teacher.learnpark.shixian.QiandaoMessages;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.StudentsUtil;
import net.learnpark.app.teacher.learnpark.util.VibratorUtil;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StudentsActivity extends ActionBarActivity implements
		OnClickListener, OnCheckedChangeListener{

	private final int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
	@SuppressWarnings("deprecation")
	private final int fp = ViewGroup.LayoutParams.FILL_PARENT;

	private FinalDb qiandaoDb;

	private String coursename;
	private String classname;
	private List<Students> stu;

	private EditText modify_studentname;
	private TextView modify_studentnum;
	private CheckBox check_girl;
	private CheckBox check_boy;
	private String checked;

	private TableLayout tableLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_students);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		// 获得传递过来的信息
		Intent intent = getIntent();
		classname = intent.getStringExtra("classname");
		coursename = intent.getStringExtra("coursename");

		tableLayout = (TableLayout) findViewById(R.id.students_tablelayout);
		TextView tv_classname = (TextView) findViewById(R.id.students_textview_classname);
		TextView tv_coursename = (TextView) findViewById(R.id.students_textview_coursename);
		tv_classname.setText("班级:" + classname);
		tv_coursename.setText("课程:" + coursename);

		stu = new ArrayList<Students>();
		qiandaoDb = FinalDb.create(this);
		List<QiandaoMessages> list_qiandao = qiandaoDb
				.findAll(QiandaoMessages.class);

		// 先获得该班级，所有学生的名单存放到stu集合里面
		stu = StudentsUtil.getStudentsList(this, classname);

		// 再从签到信息表里面，读取到总共旷课的数目
		if (list_qiandao != null && list_qiandao.size() > 0) {
			for (int i = 0; i < list_qiandao.size(); i++) {
				if (stu != null && stu.size() > 0) {
					for (int j = 0; j < stu.size(); j++) {
						if (stu.get(j).getStudentnum()
								.equals(list_qiandao.get(i).getStunum())) {
							if (!list_qiandao.get(i).getComeornot()) {
								if (stu.get(j).getRemark() == null) {
									stu.get(j).setRemark(1 + "");
								} else {
									stu.get(j)
											.setRemark(
													1
															+ Integer
																	.valueOf(stu
																			.get(j)
																			.getRemark())
															+ "");
								}
							} else {
								if (stu.get(j).getRemark() == null) {
									stu.get(j).setRemark(0 + "");
								}
							}
						}
					}
				} else {
					// 说明学生信息里面没有学生
				}
			}
		}

		// 接下来把得到的签到统计信息显示到界面上
		if (stu != null && stu.size() > 0) {
			for (int i = 1; i <= stu.size(); i++) {
				TableRow tableRow = new TableRow(this);
				tableRow = geTableRow(stu.get(i - 1), i);
				tableRow.setId(i);
				tableRow.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						modifyStudentMessage(arg0.getId());
						Log.d("TAG", arg0.getId() + "   " + 222);
					}
				});
				// 新建的TableRow添加到TableLayout
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(fp,
						wc));
			}
		} else {
			Toast.makeText(this, "班级内没有学生信息", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

	}

	public TableRow geTableRow(Students students, int i) {
		TableRow tableRow = new TableRow(this);

		TextView tv_name = new TextView(this);
		tv_name.setId(i * 1000 + 1);
		tv_name.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_name.setGravity(Gravity.CENTER);
		tv_name.setText(students.getStudentname());
		tableRow.addView(tv_name);

		TextView tv_num = new TextView(this);
		tv_num.setId(i * 1000 + 2);
		tv_num.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_num.setGravity(Gravity.CENTER);
		tv_num.setText(students.getStudentnum());
		tableRow.addView(tv_num);

		TextView tv_sex = new TextView(this);
		tv_sex.setId(i * 1000 + 3);
		tv_sex.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_sex.setGravity(Gravity.CENTER);
		tv_sex.setText(students.getSex());
		tableRow.addView(tv_sex);

		TextView tv_sum = new TextView(this);
		tv_sum.setId(i * 1000 + 4);
		tv_sum.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_sum.setGravity(Gravity.CENTER);
		tv_sum.setText(students.getRemark());
		tableRow.addView(tv_sum);

		// TextView tv_mark = new TextView(this);
		// tv_mark.setBackgroundResource(R.drawable.table_shape_fangkuang);
		// tv_mark.setGravity(Gravity.CENTER);
		// tv_mark.setText(students.getRemark());
		// tableRow.addView(tv_mark);
		return tableRow;
	}

	private void modifyStudentMessage(final int id) {
		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(StudentsActivity.this);

		View layout = (View) LayoutInflater.from(StudentsActivity.this)
				.inflate(R.layout.modify_studentsmessages, null);
		bulider.setView(layout);
		bulider.setTitle("修改学生信息");

		modify_studentname = (EditText) layout
				.findViewById(R.id.edittext_modify_studentname);
		modify_studentnum = (TextView) layout
				.findViewById(R.id.edittext_modify_studentnum);
		check_boy = (CheckBox) layout
				.findViewById(R.id.checkbox_modify_sex_boy);
		check_girl = (CheckBox) layout
				.findViewById(R.id.checkbox_modify_sex_girl);

		final TextView tv_name = (TextView) tableLayout
				.findViewById(id * 1000 + 1);
		final TextView tv_num = (TextView) tableLayout
				.findViewById(id * 1000 + 2);
		final TextView tv_sex = (TextView) tableLayout
				.findViewById(id * 1000 + 3);

		modify_studentname.setText(tv_name.getText().toString());
		modify_studentnum.setText(tv_num.getText().toString() + "  (学号暂不支持修改)");
		if (tv_sex.getText().toString().equals("男")) {
			check_boy.setSelected(true);
			checked = "男";
			check_girl.setSelected(false);
		} else {
			check_boy.setSelected(false);
			checked = "女";
			check_girl.setSelected(true);
		}

		check_boy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1 == true) {
					checked = "男";
					check_girl.setChecked(false);
				} else {
					checked = "女";
					check_girl.setChecked(true);
				}
			}
		});
		check_girl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1 == true) {
					check_boy.setChecked(false);
					checked = "女";
				} else {
					checked = "男";
					check_boy.setChecked(true);
				}
			}
		});

		bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (modify_studentname.getText() == null) {
					VibratorUtil.Vibrate(StudentsActivity.this, 200);
					Toast.makeText(StudentsActivity.this, "请填写完整的信息",
							Toast.LENGTH_SHORT).show();
				} else if (tv_name.getText().toString()
						.equals(modify_studentname.getText().toString())
						&& tv_sex.getText().toString().equals(checked)) {
					Toast.makeText(StudentsActivity.this, "未修改信息，无法保存",
							Toast.LENGTH_SHORT).show();
				} else {
					arg0.dismiss();
					Students students = new Students();
					students.setClassname(classname);
					students.setStudentname(modify_studentname.getText()
							.toString());
					students.setStudentnum(tv_num.getText().toString());
					if (check_boy.isChecked()) {
						students.setSex("男");
						tv_sex.setText("男");
					} else {
						students.setSex("女");
						tv_sex.setText("女");
					}

					Log.d("TAG", students.getStudentnum() + "   " + 222);

					StudentsUtil.modifyStudentMessages(StudentsActivity.this,
							students);
					tv_name.setText(modify_studentname.getText().toString());
					Toast.makeText(StudentsActivity.this, "修改成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bulider.setNeutralButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				Students students = new Students();
				students.setStudentnum(tv_num.getText().toString());
				StudentsUtil.deleteStudent(StudentsActivity.this, students);
				TableRow taRow = (TableRow) tableLayout.findViewById(id);
				taRow.setVisibility(View.GONE);
				setResult(RESULT_OK, (new Intent()).setAction(10 + ""));
				Toast.makeText(StudentsActivity.this, "删除成功",
						Toast.LENGTH_SHORT).show();
			}
		});

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
}
