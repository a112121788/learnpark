package net.learnpark.app.learnpark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.learnpark.app.learnpark.entity.Exam;
import net.learnpark.app.learnpark.entity.Plan;
import net.learnpark.app.learnpark.net.NetCantants;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SaveCalendarActivity extends ActionBarActivity {

	ImageView iv_up;
	ImageView iv_down;
	TextView tv_plan_num;
	TextView tv_exam_num;
	TextView tv_plan_time;
	TextView tv_exam_time;
	TextView time;

	FinalDb examsDb;
	FinalDb plansDb;
	List<Plan> list_plan;
	List<Exam> list_exam;

	SharedPreferences sp_plan;
	SharedPreferences sp_exam;
	SharedPreferences.Editor editor_plan;
	SharedPreferences.Editor editor_exam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savecalendar);
		// actionBar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("备份与导入");
		SysApplication.getInstance().addActivity(this); 
		iv_up = (ImageView) findViewById(R.id.up_calendar);
		iv_down = (ImageView) findViewById(R.id.down_calendar);
		tv_plan_num = (TextView) findViewById(R.id.plan_localdata);
		tv_exam_num = (TextView) findViewById(R.id.exam_localdata);
		tv_plan_time = (TextView) findViewById(R.id.up_time);
		tv_exam_time = (TextView) findViewById(R.id.down_time);
		time = (TextView) findViewById(R.id.time);
		
		plansDb = FinalDb.create(this);
		examsDb = FinalDb.create(this);
		list_plan = plansDb.findAll(Plan.class);
		list_exam = examsDb.findAll(Exam.class);

		// 用户邮箱
		 final String mail = getSharedPreferences("setting", MODE_PRIVATE)
		 .getString("username", "");
//		final String mail = "用户邮箱名";// 测试使用
		
		//设置当前时间
		time.setText(getTime());
		
		// 每日计划备份时间的本地数据
		sp_plan = getSharedPreferences("plan", MODE_PRIVATE);
		editor_plan = sp_plan.edit();
		String plan_time = sp_plan.getString("plan_time", "");
		if ("".equals(plan_time)) {
			tv_plan_time.setText("未备份");
		} else {
			tv_plan_time.setText(plan_time);
		}
		// 考试提醒备份时间的本地数据
		sp_exam = getSharedPreferences("exam", MODE_PRIVATE);
		editor_exam = sp_plan.edit();
		String exam_time = sp_exam.getString("exam_time", "");
		if ("".equals(exam_time)) {
			tv_exam_time.setText("未备份");
		} else {
			tv_exam_time.setText(exam_time);
		}


		// 每日计划的本地数据的 数量
		showPlanSize();


		// 考试提醒的本地数据的 数量
		showExamSize();

		// 备份日程
		iv_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] items = new String[] { "备份每日计划", "备份考试提醒" };
				Builder builder = new AlertDialog.Builder(
						SaveCalendarActivity.this);
				builder.setTitle("请选择");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Gson gson = new Gson();
							AjaxParams params = new AjaxParams();
							try {
								if (list_plan.size() > 0) {
									String plangson = gson.toJson(list_plan);
									params.put("plangson", URLEncoder.encode(
											plangson, "utf-8"));
								} else {
									params.put("plangson",
											URLEncoder.encode("null", "utf-8"));
								}
								params.put("examgson",
										URLEncoder.encode("null", "utf-8"));
								FinalHttp fh = new FinalHttp();
								fh.post(NetCantants.SAVE_DAY_PLAN_SERVLET_URL,
										params, new AjaxCallBack<Object>() {
											@Override
											public void onSuccess(Object t) {
												super.onSuccess(t);

												String time = getTime();
												tv_plan_time.setText(time);

												editor_plan = sp_plan.edit();
												editor_plan.putString(
														"plan_time", time);
												editor_plan.commit();

												Toast.makeText(
														getApplicationContext(),
														"备份成功",
														Toast.LENGTH_SHORT)
														.show();
											}

											@Override
											public void onFailure(Throwable t,
													int errorNo, String strMsg) {
												super.onFailure(t, errorNo,
														strMsg);
												Toast.makeText(
														getApplicationContext(),
														"备份失败，请检查网络连接",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						} else if (which == 1) {
							Gson gson = new Gson();
							AjaxParams params = new AjaxParams();
							try {
								if (list_exam.size() > 0) {
									String examgson = gson.toJson(list_exam);
									params.put("examgson", URLEncoder.encode(
											examgson, "utf-8"));
								} else {
									params.put("examgson",
											URLEncoder.encode("null", "utf-8"));
								}
								params.put("plangson",
										URLEncoder.encode("null", "utf-8"));
								FinalHttp fh = new FinalHttp();
								fh.post(NetCantants.SAVE_DAY_PLAN_SERVLET_URL,
										params, new AjaxCallBack<Object>() {
											@Override
											public void onSuccess(Object t) {
												super.onSuccess(t);

												String time = getTime();
												tv_exam_time.setText(time);

												editor_exam = sp_exam.edit();
												editor_exam.putString(
														"exam_time", time);
												editor_exam.commit();

												Toast.makeText(
														getApplicationContext(),
														"备份成功",
														Toast.LENGTH_SHORT)
														.show();
											}

											@Override
											public void onFailure(Throwable t,
													int errorNo, String strMsg) {
												super.onFailure(t, errorNo,
														strMsg);
												Toast.makeText(
														getApplicationContext(),
														"备份失败，请检查网络连接",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					}
				});
				builder.setPositiveButton("取消", null);
				builder.create().show();
			}
		});

		// 恢复日程
		iv_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] items = new String[] { "恢复每日计划", "恢复考试提醒" };
				Builder builder = new AlertDialog.Builder(
						SaveCalendarActivity.this);
				builder.setTitle("请选择");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							FinalHttp fh = new FinalHttp();
							AjaxParams params = new AjaxParams();
							params.put("tabs", "0");
							params.put("user", mail);
							fh.post(NetCantants.GET_PLAN_EXAM_URL, params,
									new AjaxCallBack<Object>() {

										@Override
										public void onSuccess(Object t) {
											super.onSuccess(t);
											final List<Plan> list_plan_new = new Gson()
													.fromJson(
															t + "",
															new TypeToken<List<Plan>>() {
															}.getType());
											if (!(plansDb.findAll(Plan.class).size() > 0)) {
												if (list_plan_new.size() > 0) {
													for (Plan a : list_plan_new) {
														Plan plan = new Plan();
														plan.setMail(a
																.getMail());
														plan.setPlanName(a
																.getPlanName());
														plan.setDone(a.isDone());
														plan.setImportant(a
																.isImportant());
														plan.setId(a.getId());
														plansDb.save(plan);
													}
												} else {
													Toast.makeText(
															getApplicationContext(),
															"无备份数据",
															Toast.LENGTH_SHORT)
															.show();
												}
											} else {
												Builder builder = new AlertDialog.Builder(
														SaveCalendarActivity.this);
												builder.setTitle("是否覆盖本地数据？");
												builder.setPositiveButton("取消",
														null);
												builder.setNegativeButton(
														"确定",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																if (list_plan_new
																		.size() > 0) {
																	plansDb.deleteAll(Plan.class);
																	for (Plan a : list_plan_new) {
																		Plan plan = new Plan();
																		plan.setMail(a
																				.getMail());
																		plan.setPlanName(a
																				.getPlanName());
																		plan.setDone(a
																				.isDone());
																		plan.setImportant(a
																				.isImportant());
																		plan.setId(a
																				.getId());
																		plansDb.save(plan);
																	}
																	showPlanSize();
																} else {
																	Toast.makeText(
																			getApplicationContext(),
																			"无备份数据",
																			Toast.LENGTH_SHORT)
																			.show();
																}
															}
														});
												builder.create().show();
											}
											
											showPlanSize();
										};

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Toast.makeText(
													getApplicationContext(),
													"恢复失败，请检查网络连接",
													Toast.LENGTH_SHORT).show();
										};
									});

						} else if (which == 1) {

							FinalHttp fh = new FinalHttp();
							AjaxParams params = new AjaxParams();
							params.put("tabs", "1");
							params.put("user", mail);
							fh.post(NetCantants.GET_PLAN_EXAM_URL, params,
									new AjaxCallBack<Object>() {
										@Override
										public void onSuccess(Object t) {
											super.onSuccess(t);
											final List<Exam> list_exam_new = new Gson()
													.fromJson(
															t + "",
															new TypeToken<List<Exam>>() {
															}.getType());
											if (!(examsDb.findAll(Exam.class).size()> 0)) {
												if (list_exam_new.size() > 0) {
													for (Exam a : list_exam_new) {
														Exam exam = new Exam();
														exam.setMail(a
																.getMail());
														exam.setName(a
																.getName());
														exam.setTime(a
																.getTime());
														exam.setDone(a.isDone());
														exam.setImportant(a
																.isImportant());
														exam.setId(a.getId());
														examsDb.save(exam);
													}
												} else {
													Toast.makeText(
															getApplicationContext(),
															"无备份数据",
															Toast.LENGTH_SHORT)
															.show();
												}
											} else {
												Builder builder = new AlertDialog.Builder(
														SaveCalendarActivity.this);
												builder.setTitle("是否覆盖本地数据？");
												builder.setPositiveButton("取消",
														null);
												builder.setNegativeButton(
														"确定",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																
																if (list_exam_new
																		.size() > 0) {
																	examsDb.deleteAll(Exam.class);
																	for (Exam a : list_exam_new) {
																		Exam exam = new Exam();
																		exam.setMail(a
																				.getMail());
																		exam.setName(a
																				.getName());
																		exam.setTime(a
																				.getTime());
																		exam.setDone(a
																				.isDone());
																		exam.setImportant(a
																				.isImportant());
																		exam.setId(a
																				.getId());
																		examsDb.save(exam);
																	}
																	showExamSize();
																} else {
																	Toast.makeText(
																			getApplicationContext(),
																			"无备份数据",
																			Toast.LENGTH_SHORT)
																			.show();
																}
															}
														});
												builder.create().show();
											}
											showExamSize();
										};

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Toast.makeText(
													getApplicationContext(),
													"恢复失败，请检查网络连接",
													Toast.LENGTH_SHORT).show();
										};

									});

						}
					}
				});
				builder.setPositiveButton("取消", null);
				builder.create().show();
			}
		});

	}
	/**
	 * 显示考试提醒数据的多少
	 */
	private void showPlanSize() {
		int plan_num = plansDb.findAll(Plan.class).size();
		if (plan_num > 0) {
			tv_plan_num.setText(plan_num + "");
		} else {
			tv_plan_num.setText("0");
		}
		
	}
	/**
	 * 显示考试提醒数据的多少
	 */
	private void showExamSize() {

		int exam_num = examsDb.findAll(Exam.class).size();
		if (exam_num > 0) {
			tv_exam_num.setText(exam_num + "");
		} else {
			tv_exam_num.setText("0");
		}
	}

	private String getTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String time = year + "年" + (month + 1) + "月" + day + "日";
		return time;
	}
}
