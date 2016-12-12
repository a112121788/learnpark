package net.learnpark.app.learnpark;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.learnpark.app.learnpark.entity.Teacher;
import net.tsz.afinal.FinalDb;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 教师信息查询和编辑
 * 
 * @author peng
 * @version 1 2014-6-12 14:12:46
 */
public class TeacherInfoActivity extends ActionBarActivity {
	TextView noteacher;
	ListView teachers;
	FinalDb teacherDb;
	List<Teacher> list_teacher;
	LinkedList<Map<String, String>> data;
	SimpleAdapter sAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacherinfo);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		SysApplication.getInstance().addActivity(this); 
		teachers = (ListView) findViewById(R.id.teachers);
		noteacher = (TextView) findViewById(R.id.noteacher);
		data = new LinkedList<Map<String, String>>();
		teacherDb = FinalDb.create(this);
		list_teacher = teacherDb.findAll(Teacher.class);

		if (list_teacher != null) {
			for (int i = 0; i < list_teacher.size(); i++) {
				Map<String, String> map = new ArrayMap<String, String>();
				map.put("teachername", list_teacher.get(i).getTeachername());
				map.put("teacherphone", list_teacher.get(i).getTeacherphone());
				map.put("teacheremail", list_teacher.get(i).getTeacheremail());
				data.addFirst(map);
			}
		} else {
			noteacher.setVisibility(View.VISIBLE);
		}
		String[] from = { "teachername", "teacherphone", "teacheremail" };
		int[] to = { R.id.teachername, R.id.teacherphone, R.id.teacheremail };
		sAdapter = new SimpleAdapter(getApplicationContext(), data,
				R.layout.item_teacher, from, to);
		teachers.setAdapter(sAdapter);

		teachers.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(
						TeacherInfoActivity.this);
				builder.setTitle("删除该老师？");
				final AlertDialog dialog1;
				builder.setPositiveButton("是的",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								teacherDb.deleteByWhere(
										Teacher.class,
										"teachername='"
												+ data.get(position).get(
														"teachername") + "'");
								data.remove(position);
								sAdapter.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(), "删除成功",
										1).show();
							}
						});
				builder.setNegativeButton("取消", null);
				dialog1 = builder.create();
				dialog1.show();

				return true;
			}
		});
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.teacherinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.teacher_add) {
			AlertDialog.Builder builder = new Builder(TeacherInfoActivity.this);
			builder.setTitle("新增我的老师");
			builder.setIcon(R.drawable.ic_launcher);
			final View layout = getLayoutInflater().inflate(
					R.layout.popup_add_teacher, null);
			EditText teachername_add = (EditText) layout
					.findViewById(R.id.teachername_add);
			EditText teacherphone_add = (EditText) layout
					.findViewById(R.id.teacherphone_add);
			EditText teacheremail_add = (EditText) layout
					.findViewById(R.id.teacheremail_add);
			builder.setView(layout);
			builder.setPositiveButton("添加",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							EditText teachername_add = (EditText) layout
									.findViewById(R.id.teachername_add);
							EditText teacherphone_add = (EditText) layout
									.findViewById(R.id.teacherphone_add);
							EditText teacheremail_add = (EditText) layout
									.findViewById(R.id.teacheremail_add);
							Teacher teacher = new Teacher();
							teacher.setTeachername(teachername_add.getText()
									.toString());
							teacher.setTeacherphone(teacherphone_add.getText()
									.toString());
							teacher.setTeacheremail(teacheremail_add.getText()
									.toString());
							teacherDb.save(teacher);
							Toast.makeText(getApplicationContext(), "添加成功", 2)
									.show();
							noteacher.setVisibility(View.GONE);
							Map<String, String> map = new ArrayMap<String, String>();
							map.put("teachername", teacher.getTeachername());
							map.put("teacherphone", teacher.getTeacherphone());
							map.put("teacheremail", teacher.getTeacheremail());
							data.addFirst(map);
							sAdapter.notifyDataSetChanged();
						}
					});
			builder.setNegativeButton("取消", null);
			AlertDialog dialog2 = builder.create();
			dialog2.show();
		}
		return super.onOptionsItemSelected(item);
	}

}
