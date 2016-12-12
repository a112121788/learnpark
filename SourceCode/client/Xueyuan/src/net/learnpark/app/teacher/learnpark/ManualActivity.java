package net.learnpark.app.teacher.learnpark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.learnpark.app.teacher.learnpark.shixian.QiandaoMessages;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.StudentsUtil;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ManualActivity extends ActionBarActivity implements
		OnCheckedChangeListener {

	private String coursename;
	private String classname;
	private List<Students> stu;
	private String date;

	private final int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
	@SuppressWarnings("deprecation")
	private final int fp = ViewGroup.LayoutParams.FILL_PARENT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_manual);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		// 获得传递过来的信息
		Intent intent = getIntent();
		classname = intent.getStringExtra("classname");
		coursename = intent.getStringExtra("coursename");

		// 获得当前系统的时间，存入数据库
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		date = year + "-" + month + "-" + day;

		TableLayout tableLayout = (TableLayout) findViewById(R.id.dianming_table_manual);
		stu = new ArrayList<Students>();

		stu = StudentsUtil.getStudentsList(this, classname);
		// 全部列自动填充空白处
		tableLayout.setStretchAllColumns(true);
		// 接下来把得到的签到统计信息显示到界面上
		if (stu != null && stu.size() > 0) {
			for (int i = 0; i < stu.size(); i++) {
				TableRow tableRow = new TableRow(this);
				tableRow = geTableRow(stu.get(i), i);
				// 新建的TableRow添加到TableLayout
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(fp,
						wc));
			}
		} else {
			Toast.makeText(this, "班级内没有学生信息", Toast.LENGTH_SHORT).show();
		}

		// for (int row = 0; row < m; row++) {
		// TableRow tableRow = new TableRow(ManualActivity.this);
		// for (int col = 0; col < n - 1; col++) {
		// TextView tv = new TextView(ManualActivity.this);
		// tv.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		// x += 1;
		// tv.setGravity(Gravity.CENTER);
		// tv.setText(x + "");
		// tableRow.addView(tv);
		// }
		// CheckBox tv = new CheckBox(ManualActivity.this);
		// tv.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		// tv.setGravity(Gravity.CENTER);
		// tableRow.addView(tv);
		// // 新建的TableRow添加到TableLayout
		// tableLayout.addView(tableRow, new TableLayout.LayoutParams(fp, wc));
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		MenuItem mnu = menu.add(0, 0, 0, "提交点名结果");
		{
			MenuItemCompat.setShowAsAction(mnu,
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS
							| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == 0) {
			PutInSql();
			Toast.makeText(ManualActivity.this, "提交成功", Toast.LENGTH_SHORT)
					.show();
		}
		return super.onOptionsItemSelected(item);

	}

	public TableRow geTableRow(Students students, int id) {
		TableRow tableRow = new TableRow(this);

		TextView tv_name = new TextView(this);
		tv_name.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_name.setGravity(Gravity.CENTER);
		tv_name.setTextSize(16);
		tv_name.setText(students.getStudentname());
		tableRow.addView(tv_name);

		TextView tv_num = new TextView(this);
		tv_num.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_num.setGravity(Gravity.CENTER);
		tv_num.setTextSize(16);
		tv_num.setText(students.getStudentnum());
		tableRow.addView(tv_num);

		TextView tv_sex = new TextView(this);
		tv_sex.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		tv_sex.setGravity(Gravity.CENTER);
		tv_sex.setTextSize(16);
		tv_sex.setText(students.getSex());
		tableRow.addView(tv_sex);

		// TextView tv_sum = new TextView(this);
		// tv_sum.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		// tv_sum.setGravity(Gravity.CENTER);
		// tv_sum.setText(students.getAbsencesum());
		// tableRow.addView(tv_sum);

		CheckBox cb = new CheckBox(ManualActivity.this);
		cb.setBackgroundResource(R.drawable.table_shape_fangkuang1);
		cb.setId(id);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.d("TAG", arg0.getId() + "  " + arg1);
				ChangeTheState(arg0.getId(), arg1);
			}
		});
		cb.setGravity(Gravity.CENTER);
		tableRow.addView(cb);

		return tableRow;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	private void ChangeTheState(int id, Boolean bl) {
		if (bl == true) {
			stu.get(id).setRemark("yes");
		} else {
			stu.get(id).setRemark("no");
		}
	}

	public void PutInSql() {
		// 点击签到结束后将签到信息存入数据库内
		FinalDb qiandaoDb = FinalDb.create(ManualActivity.this);
		for (int i = 0; i < stu.size(); i++) {
			QiandaoMessages qiandaoMessages = new QiandaoMessages();
			qiandaoMessages.setDate(date);
			qiandaoMessages.setClassname(classname);
			qiandaoMessages.setCoursename(coursename);
			qiandaoMessages.setStunum(stu.get(i).getStudentnum());
			qiandaoMessages.setImei("手动");
			if (stu.get(i).getRemark() != null
					&& stu.get(i).getRemark().equals("yes")) {
				qiandaoMessages.setComeornot(true);
			} else {
				qiandaoMessages.setComeornot(false);
			}
			qiandaoDb.save(qiandaoMessages);
		}
	}
}
