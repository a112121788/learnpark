package net.learnpark.app.teacher.learnpark;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.util.ClassesUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddStudentsActivity extends ActionBarActivity implements
		OnClickListener, OnCheckedChangeListener {

	private EditText ed_add_studentname;
	private EditText ed_add_studentnum;
	private EditText ed_add_studentnum_front;
	private EditText ed_add_studentnum_begin;
	private EditText ed_add_studentnum_end;

	private ArrayAdapter<String> selectClassAdapter;
	private Spinner selectClassSpinner;

	private FinalDb classDb;
	private EditText add_coursename;// 课程名
	private EditText add_classname; // 班级名称
	private EditText add_department; // 所属院系

	private CheckBox check_girl;
	private CheckBox check_boy;
	private String[] data;
	private String classname = null;
	private Boolean single_onoff = true;
	private Boolean group_onoff = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 主题颜色 by高帅朋
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_addstudents);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		ed_add_studentname = (EditText) findViewById(R.id.edittext_add_studentname);
		ed_add_studentnum = (EditText) findViewById(R.id.edittext_add_studentnum);
		ed_add_studentnum_front = (EditText) findViewById(R.id.edittext_add_studentsnum_front);
		ed_add_studentnum_begin = (EditText) findViewById(R.id.edittext_add_studentsnum_begin);
		ed_add_studentnum_end = (EditText) findViewById(R.id.edittext_add_studentsnum_end);

		// 初始化data集合
		getClasses_data();

		selectClassAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		selectClassAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectClassSpinner = (Spinner) findViewById(R.id.spinner_class);
		selectClassSpinner.setAdapter(selectClassAdapter);
		selectClassSpinner
				.setOnItemSelectedListener(new SpinneronSelectedListener());

		// 设置添加班级按钮的监听函数
		ImageView btn_addclass = (ImageView) findViewById(R.id.btn_img_add_class);
		btn_addclass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog_addclasses();
			}
		});

		// 设置选择性别的监听事件
		check_boy = (CheckBox) findViewById(R.id.checkbox_add_sex_boy);
		check_girl = (CheckBox) findViewById(R.id.checkbox_add_sex_girl);
		check_boy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1 == true) {
					check_girl.setChecked(false);
				} else {
					check_girl.setChecked(true);
				}
			}
		});
		check_girl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1 == true) {
					check_boy.setChecked(false);
				} else {
					check_boy.setChecked(true);
				}
			}
		});

		// 设置快速添加，和单个添加的隐藏和显示
		final LinearLayout lil_single = (LinearLayout) findViewById(R.id.add_students_single_lil);
		final LinearLayout lil_group = (LinearLayout) findViewById(R.id.add_students_group_lil);
		final LinearLayout lil_single_onoff = (LinearLayout) findViewById(R.id.add_students_single_lilonoff);
		final LinearLayout lil_group_onoff = (LinearLayout) findViewById(R.id.add_students_group_lilonoff);
		final RelativeLayout rel_btn_single = (RelativeLayout) findViewById(R.id.add_students_single_onoff);
		final RelativeLayout rel_btn_group = (RelativeLayout) findViewById(R.id.add_students_group_onoff);
		final ImageView img_single = (ImageView) findViewById(R.id.add_students_single_image_onoff);
		final ImageView img_group = (ImageView) findViewById(R.id.add_students_group_image_onoff);

		rel_btn_single.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (group_onoff == false) {
					lil_single_onoff.setVisibility(View.GONE);
					img_single.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
					lil_group_onoff.setVisibility(View.VISIBLE);
					img_group.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
					lil_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					lil_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor_shape);
					rel_btn_group
							.setBackgroundResource(R.drawable.table_shape_button);
					rel_btn_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					single_onoff = false;
					group_onoff = true;
				} else {
					lil_single_onoff.setVisibility(View.VISIBLE);
					img_single.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
					lil_group_onoff.setVisibility(View.GONE);
					img_group.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
					lil_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor_shape);
					lil_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					rel_btn_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					rel_btn_single
							.setBackgroundResource(R.drawable.table_shape_button);
					single_onoff = true;
					group_onoff = false;
				}
			}
		});
		rel_btn_group.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (single_onoff == true) {
					lil_single_onoff.setVisibility(View.GONE);
					img_single.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
					lil_group_onoff.setVisibility(View.VISIBLE);
					img_group.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
					lil_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					lil_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor_shape);
					rel_btn_group
							.setBackgroundResource(R.drawable.table_shape_button);
					rel_btn_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					single_onoff = false;
					group_onoff = true;
				} else {
					lil_single_onoff.setVisibility(View.VISIBLE);
					img_single.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_close));
					lil_group_onoff.setVisibility(View.GONE);
					img_group.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_notice_open));
					lil_single
							.setBackgroundResource(R.drawable.table_edittext_nocolor_shape);
					lil_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					rel_btn_group
							.setBackgroundResource(R.drawable.table_edittext_nocolor);
					rel_btn_single
							.setBackgroundResource(R.drawable.table_shape_button);
					single_onoff = true;
					group_onoff = false;
				}
			}
		});

		Button btn_add_student_single = (Button) findViewById(R.id.btn_add_student_single);
		Button btn_add_students_group = (Button) findViewById(R.id.btn_add_students_group);
		btn_add_student_single.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ed_add_studentname.getText().toString().equals("")
						|| ed_add_studentnum.getText().toString().equals("")) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "请填写完整的信息",
							Toast.LENGTH_SHORT).show();
				} else if (classname == null || classname.length() == 0) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "请先添加班级",
							Toast.LENGTH_SHORT).show();

				} else if (!isNumeric(ed_add_studentnum.getText().toString())) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "学号只能是数字",
							Toast.LENGTH_SHORT).show();
				} else {
					Students students = new Students();
					students.setClassname(classname);
					students.setStudentname(ed_add_studentname.getText()
							.toString());
					students.setStudentnum(ed_add_studentnum.getText()
							.toString());
					if (check_boy.isChecked()) {
						students.setSex("男");
					} else {
						students.setSex("女");
					}
					int returnid = StudentsUtil.insertStudents(
							AddStudentsActivity.this, students);
					if (returnid == 2) {
						VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
						Toast.makeText(AddStudentsActivity.this,
								"您添加的学生已经添加过了", Toast.LENGTH_SHORT).show();
					} else if (returnid == 1) {
						Toast.makeText(AddStudentsActivity.this, "添加成功",
								Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK, (new Intent()).setAction(8 + ""));
					}
				}
			}
		});

		btn_add_students_group.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ed_add_studentnum_front.getText().toString().equals("")
						|| ed_add_studentnum_begin.getText().toString()
								.equals("")
						|| ed_add_studentnum_end.getText().toString()
								.equals("")) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "请填写完整的信息",
							Toast.LENGTH_SHORT).show();
				} else if (classname == null || classname.length() == 0) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "请先添加班级",
							Toast.LENGTH_SHORT).show();

				} else if ((!isNumeric(ed_add_studentnum_front.getText()
						.toString()))
						|| (!isNumeric(ed_add_studentnum_begin.getText()
								.toString()))
						|| (!isNumeric(ed_add_studentnum_end.getText()
								.toString()))) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "学号只能是数字",
							Toast.LENGTH_SHORT).show();
				} else if (Integer.valueOf(ed_add_studentnum_begin.getText()
						.toString()) > Integer.valueOf(ed_add_studentnum_end
						.getText().toString())) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "开始编号不能比结束编号大",
							Toast.LENGTH_SHORT).show();
				} else if (Integer.valueOf(ed_add_studentnum_end.getText()
						.toString())
						- Integer.valueOf(ed_add_studentnum_begin.getText()
								.toString()) > 999) {
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "无法添加这么多学生",
							Toast.LENGTH_SHORT).show();
				} else {
					String front = ed_add_studentnum_front.getText().toString();
					int begin = Integer.valueOf(ed_add_studentnum_begin
							.getText().toString());
					int end = Integer.valueOf(ed_add_studentnum_end.getText()
							.toString());
					List<Students> students_list = new ArrayList<Students>();
					for (int i = begin; i <= end; i++) {
						Students students = new Students();
						students.setClassname(classname);
						if ((end + "").length() - (i + "").length() == 2) {
							students.setStudentnum(front + "00" + i);
						} else if ((end + "").length() - (i + "").length() == 1) {
							students.setStudentnum(front + "0" + i);
						} else if ((end + "").length() - (i + "").length() == 0) {
							students.setStudentnum(front + i);
						}
						students_list.add(students);
					}
					setResult(RESULT_OK, (new Intent()).setAction(8 + ""));
					Boolean istrue = StudentsUtil.insertStudents(
							AddStudentsActivity.this, students_list);
					if (istrue) {
						setResult(RESULT_OK, (new Intent()).setAction(8 + ""));
						Toast.makeText(AddStudentsActivity.this, "添加成功",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	// 获得spinner内的data数组
	private void getClasses_data() {
		List<Classes> classes_list = ClassesUtil
				.getClassesList(AddStudentsActivity.this);
		List<String> data_list = new ArrayList<String>();
		for (Classes classes : classes_list) {
			if (data_list != null && data_list.size() > 0) {
				for (int i = 0; i < data_list.size(); i++) {
					if (classes.getClassname().equals(data_list.get(i))) {
						break;
					} else if (i == data_list.size() - 1) {
						data_list.add(classes.getClassname());
						break;
					}
				}
			} else {
				data_list.add(classes.getClassname());
			}
		}
		data = new String[data_list.size()];
		for (int i = 0; i < data_list.size(); i++) {
			data[i] = data_list.get(i);
		}
	}

	class SpinneronSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			classname = data[position];
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	// 判断是否为纯数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 添加班级的对话框
	public void AlertDialog_addclasses() {

		AlertDialog alertDialog;
		AlertDialog.Builder bulider = new Builder(AddStudentsActivity.this);
		bulider.setIcon(R.drawable.ic_launcher);

		View layout = (View) LayoutInflater.from(AddStudentsActivity.this)
				.inflate(R.layout.add_class, null);
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
					VibratorUtil.Vibrate(AddStudentsActivity.this, 200);
					Toast.makeText(AddStudentsActivity.this, "请填写完整的信息",
							Toast.LENGTH_SHORT).show();
				} else {
					if (data != null && data.length > 0) {

						for (int i = 0; i < data.length; i++) {
							if (data[i].equals(add_classname.getText()
									.toString())) {
								VibratorUtil.Vibrate(AddStudentsActivity.this,
										200);
								Toast.makeText(AddStudentsActivity.this,
										"您所添加的班级已经添加过了", Toast.LENGTH_SHORT)
										.show();
								break;
							} else if (i == data.length - 1) {
								dialog.dismiss();
								classDb = FinalDb
										.create(AddStudentsActivity.this);
								Classes classes = new Classes();
								classes.setClassname(add_classname.getText()
										.toString());
								classes.setCoursename(add_coursename.getText()
										.toString());
								classes.setDepartment(add_department.getText()
										.toString());
								classDb.save(classes);

								String[] thisdata = data;
								data = new String[thisdata.length + 1];
								for (int j = 0; j < thisdata.length; j++) {
									data[j] = thisdata[j];
								}
								data[data.length - 1] = add_classname.getText()
										.toString();
								selectClassAdapter = new ArrayAdapter<String>(
										AddStudentsActivity.this,
										android.R.layout.simple_spinner_item,
										data);
								selectClassAdapter
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								selectClassSpinner
										.setAdapter(selectClassAdapter);
								selectClassAdapter.notifyDataSetChanged();
								selectClassSpinner
										.setSelection(data.length - 1);
								classname = add_classname.getText().toString();
								setResult(RESULT_OK,
										(new Intent()).setAction(8 + ""));
								break;
							}
						}
					} else {
						dialog.dismiss();
						classDb = FinalDb.create(AddStudentsActivity.this);
						Classes classes = new Classes();
						classes.setClassname(add_classname.getText().toString());
						classes.setCoursename(add_coursename.getText()
								.toString());
						classes.setDepartment(add_department.getText()
								.toString());
						classDb.save(classes);

						data = new String[1];
						data[0] = add_classname.getText().toString();
						selectClassAdapter = new ArrayAdapter<String>(
								AddStudentsActivity.this,
								android.R.layout.simple_spinner_item, data);
						selectClassAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						selectClassSpinner.setAdapter(selectClassAdapter);
						selectClassAdapter.notifyDataSetChanged();
						selectClassSpinner.setSelection(0);
						setResult(RESULT_OK, (new Intent()).setAction(8 + ""));
						classname = add_classname.getText().toString();
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
