package net.learnpark.app.learnpark.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.learnpark.app.learnpark.R;
import net.learnpark.app.learnpark.entity.Exam;
import net.learnpark.app.learnpark.entity.Plan;
import net.learnpark.app.learnpark.net.NetCantants;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 在主界面点击日程时的显示内容 我的任务（高帅朋）
 * 
 * @author peng
 * 
 */
@SuppressLint("ValidFragment")
public class ScheduleFragment extends Fragment {
	TabHost tabHost;
	private SimpleAdapter plansAdapter; // 计划的适配器
	private SimpleAdapter examsAdapter;// 考试的的适配器
	private PullToRefreshListView plansPullToRefreshListView;// 计划下拉刷新控件
	private PullToRefreshListView examsPullToRefreshListView;// 考试 下拉刷新控件
	private LinkedList<Plan> plansListItems; // 存放计划数据
	private LinkedList<Exam> examsListItems; // 存放考试计划
	private boolean isDone;
	private boolean isImportant;
	ArrayList<Map<String, Object>> list;
	ArrayList<Map<String, Object>> list1;
	FinalDb examsDb;
	FinalDb plansDb;
	List<Plan> list_plan;
	List<Plan> list_plan_new;
	List<Exam> list_exam;
	List<Exam> list_exam_new;
	EditText planNameet;
	EditText examNameet;
	EditText examTimeet;
	ImageButton schedule_add1; // 添加
	ImageButton schedule_add2;
	String mail;

	// 用户邮箱
	// final String mail = getActivity().getSharedPreferences("setting",
	// Context.MODE_PRIVATE).getString("username", "");

	public ScheduleFragment() {
	}

	public ScheduleFragment(LinkedList<Plan> plansListItems,
			LinkedList<Exam> examsListItems) {
		this.plansListItems = plansListItems;
		this.examsListItems = examsListItems;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container,
				false);
		schedule_add1 = (ImageButton) view.findViewById(R.id.schedule_add1);
		schedule_add2 = (ImageButton) view.findViewById(R.id.schedule_add2);

		schedule_add1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (mail != null) {
					AlertDialog alertDialog;
					AlertDialog.Builder bulider = new Builder(getActivity());
					bulider.setIcon(R.drawable.ic_launcher);
					View layoutPlan = (View) LayoutInflater.from(getActivity())
							.inflate(R.layout.schedule_add_plan, null);
					bulider.setView(layoutPlan);
					planNameet = (EditText) layoutPlan
							.findViewById(R.id.add_planname);
					bulider.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									String planName = planNameet.getText()
											.toString();
									Plan plan = new Plan();
									plan.setPlanName(planName);
									plan.setDone(false);
									plan.setImportant(true);
									plan.setMail(mail);
									plansDb.save(plan);

									send();
									planRefresh();
								}
							});
					bulider.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					alertDialog = bulider.create();
					alertDialog.show();
				} else {
					Toast.makeText(getActivity(), "登陆后才可以添加", 2).show();
				}
			}
		});
		schedule_add2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (mail != null) {
					AlertDialog alertDialog;
					AlertDialog.Builder bulider = new Builder(getActivity());
					bulider.setIcon(R.drawable.ic_launcher);
					View layoutExam = LayoutInflater.from(getActivity())
							.inflate(R.layout.schedule_add_exam, null);
					bulider.setView(layoutExam);

					examNameet = (EditText) layoutExam
							.findViewById(R.id.add_examname);
					examTimeet = (EditText) layoutExam
							.findViewById(R.id.add_time);
					Calendar calendar = Calendar.getInstance();
					final int year = calendar.get(Calendar.YEAR);
					final int month = calendar.get(Calendar.MONTH);
					final int day = calendar.get(Calendar.DAY_OF_MONTH);
					examTimeet.setText(year + "年" + (month + 1) + "月" + day
							+ "日");
					examTimeet.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							new DatePickerDialog(getActivity(),
									new DatePickerDialog.OnDateSetListener() {

										@Override
										public void onDateSet(DatePicker view,
												int year, int monthOfYear,
												int dayOfMonth) {
											examTimeet.setText(year + "年"
													+ (monthOfYear + 1) + "月"
													+ dayOfMonth + "日");
										}
									}, year, month, day).show();
						}
					});
					bulider.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String examName = examNameet.getText()
											.toString();

									String examdate = examTimeet.getText()
											.toString();
									Exam exam = new Exam();
									exam.setName(examName);
									exam.setTime(examdate);// 应当获得输入时间，未实现
									exam.setMail(mail);
									exam.setDone(false);
									exam.setImportant(true);
									examsDb.save(exam);

									send();
									examRefresh();
								}
							});
					bulider.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					alertDialog = bulider.create();
					alertDialog.show();
				} else {
					Toast.makeText(getActivity(), "登陆后才可以添加", 2).show();
				}
			}
		});

		if (!getActivity()
				.getSharedPreferences("setting", Context.MODE_PRIVATE)
				.getString("username", "").equals("")) {
			mail = getActivity().getSharedPreferences("setting",
					Context.MODE_PRIVATE).getString("username", "");

		}

		// 计划 和考试
		// 计划
		tabHost = (TabHost) view.findViewById(R.id.all_columns);
		tabHost.setup();
		TabSpec my_tab1 = tabHost.newTabSpec("schedule_plans")
				.setIndicator("每日计划").setContent(R.id.schedule_plans_box);
		// 考试
		tabHost.addTab(my_tab1);
		TabSpec my_tab2 = tabHost.newTabSpec("schedule_exams")
				.setIndicator("考试提醒").setContent(R.id.schedule_exams_box);
		tabHost.addTab(my_tab2);

		// 数据库里面的信息
		plansDb = FinalDb.create(getActivity());
		examsDb = FinalDb.create(getActivity());
		list_plan = plansDb.findAll(Plan.class);
		list_exam = examsDb.findAll(Exam.class);

		// 每日计划
		plansPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.schedule_plans);
		if (mail != null) {
			plansPullToRefreshListView
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						boolean a = true;

						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							String label = DateUtils.formatDateTime(
									getActivity(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME
											| DateUtils.FORMAT_SHOW_DATE
											| DateUtils.FORMAT_ABBREV_ALL);
							refreshView.getLoadingLayoutProxy()
									.setLastUpdatedLabel(label);

							FinalHttp fh = new FinalHttp();
							AjaxParams applan = new AjaxParams();
							applan.put("tabs", "0");
							applan.put("user", mail);
							fh.post(NetCantants.GET_PLAN_EXAM_URL, applan,
									new AjaxCallBack<Object>() {
										@Override
										public void onSuccess(Object t) {
											super.onSuccess(t);
											list_plan_new = new Gson()
													.fromJson(
															t + "",
															new TypeToken<List<Plan>>() {
															}.getType());
											if (list_plan.isEmpty()) {
												a = false;
												for (Plan a : list_plan_new) {
													Plan plan = new Plan();
													plan.setMail(a.getMail());
													plan.setPlanName(a
															.getPlanName());
													plan.setDone(a.isDone());
													plan.setImportant(a
															.isImportant());
													plan.setId(a.getId());
													plansDb.save(plan);
												}

												list_plan = plansDb
														.findAll(Plan.class);
												list = new ArrayList<>();
												if (list_plan_new != null
														&& list_plan_new.size() > 0) {
													for (int i = 0; i < list_plan_new
															.size(); i++) {
														Map<String, Object> map = new ArrayMap<String, Object>();
														map.put("isDone",
																list_plan
																		.get(i)
																		.isDone());
														if (list_plan_new
																.get(i)
																.isDone()) {
															map.put("done",
																	R.drawable.done);
														} else {
															map.put("done",
																	R.drawable.undo);
														}
														map.put("mail", mail);
														map.put("planname",
																list_plan_new
																		.get(i)
																		.getPlanName());
														map.put("isImportant",
																list_plan_new
																		.get(i)
																		.isImportant());
														if (list_plan_new
																.get(i)
																.isImportant()) {
															map.put("important",
																	R.drawable.important);
														} else {
															map.put("important",
																	R.drawable.unimportant);
														}
														list.add(map);
													}
												} else {
													Map<String, Object> map = new ArrayMap<String, Object>();
													map.put("done",
															R.drawable.done);
													map.put("isDone", false);
													map.put("planname",
															"抱歉，账号无数据");
													map.put("isImportant", true);
													map.put("important",
															R.drawable.important);
													list.add(map);
												}
												String[] from = { "done",
														"planname", "important" };
												int[] to = { R.id.done,
														R.id.planname,
														R.id.important };
												plansAdapter = new SimpleAdapter(
														getActivity(), list,
														R.layout.item_plan,
														from, to);
												plansPullToRefreshListView
														.setAdapter(plansAdapter);
												plansAdapter
														.notifyDataSetChanged();
												plansPullToRefreshListView
														.onRefreshComplete();
											} else {
												planRefresh();
												send();
											}
											if (a) {
												Toast.makeText(getActivity(),
														"最新数据",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(getActivity(),
														"刷新成功",
														Toast.LENGTH_SHORT)
														.show();
											}
										}

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Toast.makeText(getActivity(),
													"无网络连接", Toast.LENGTH_SHORT)
													.show();

											planRefresh();

										}

									});
						}
					});
		} else {

		}
		planRefresh();

		plansPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {

						if (mail != null) {
							final TextView pn = (TextView) view
									.findViewById(R.id.planname);

							final String planname = pn.getText().toString();
							final boolean checkname = "无数据".equals(planname)
									& "抱歉，账号无数据".equals(planname);
							isImportant = (boolean) list.get(position - 1).get(
									"isImportant");
							isDone = (boolean) list.get(position - 1).get(
									"isDone");

							final Plan plan = new Plan();
							plan.setMail(mail);
							plan.setPlanName(planname);

							final ImageView done = (ImageView) view
									.findViewById(R.id.done);

							final ImageView important = (ImageView) view
									.findViewById(R.id.important);
							pn.setOnLongClickListener(new OnLongClickListener() {

								@Override
								public boolean onLongClick(View v) {
									AlertDialog.Builder bulider = new Builder(
											getActivity());
									bulider.setTitle("是否刪除！")
											.setNegativeButton(
													"刪除",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															plansDb.deleteByWhere(
																	Plan.class,
																	"planName=\""
																			+ planname
																			+ "\"");
															Toast.makeText(
																	getActivity(),
																	"已刪除，请刷新数据!",
																	Toast.LENGTH_SHORT)
																	.show();
															send();
														}

													})
											.setPositiveButton("取消", null)
											.show();
									return true;
								}
							});
							done.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									if (isDone) {
										done.setImageResource(R.drawable.undo);
										list.get(position - 1).put("isDone",
												false);
										isDone = !isDone;
										plan.setDone(isDone);
										plan.setImportant(isImportant);
										if (!checkname) {
											plansDb.update(plan, "planName=\""
													+ planname + "\"");
										}
									} else {
										done.setImageResource(R.drawable.done);
										list.get(position - 1).put("isDone",
												true);
										isDone = !isDone;
										plan.setDone(isDone);
										plan.setImportant(isImportant);
										if (!checkname) {
											plansDb.update(plan, "planName=\""
													+ planname + "\"");
										}
									}
								}
							});
							important.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									if (isImportant) {
										important
												.setImageResource(R.drawable.unimportant);
										isImportant = !isImportant;
										list.get(position - 1).put(
												"isImportant", false);
										plan.setDone(isDone);
										plan.setImportant(isImportant);
										if (!checkname) {
											plansDb.update(plan, "planName=\""
													+ planname + "\"");
										}
									} else {
										important
												.setImageResource(R.drawable.important);
										list.get(position - 1).put(
												"isImportant", true);
										isImportant = !isImportant;
										plan.setDone(isDone);
										plan.setImportant(isImportant);
										if (!checkname) {
											plansDb.update(plan, "planName=\""
													+ planname + "\"");
										}
									}
								}
							});
						}
					}
				});

		// 考试提醒
		examsPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.schedule_exams);
		if (mail != null) {
			examsPullToRefreshListView
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						boolean a = true;

						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {

							String label = DateUtils.formatDateTime(
									getActivity(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME
											| DateUtils.FORMAT_SHOW_DATE
											| DateUtils.FORMAT_ABBREV_ALL);
							refreshView.getLoadingLayoutProxy()
									.setLastUpdatedLabel(label);

							FinalHttp fh = new FinalHttp();
							AjaxParams applan = new AjaxParams();
							applan.put("tabs", "1");
							applan.put("user", mail);
							fh.post(NetCantants.GET_PLAN_EXAM_URL, applan,
									new AjaxCallBack<Object>() {
										@Override
										public void onSuccess(Object t) {
											super.onSuccess(t);
											list_exam_new = new Gson()
													.fromJson(
															t + "",
															new TypeToken<List<Exam>>() {
															}.getType());
											if (list_exam.isEmpty()) {
												a = false;
												for (Exam a : list_exam_new) {
													Exam exam = new Exam();
													exam.setMail(a.getMail());
													exam.setName(a.getName());
													exam.setTime(a.getTime());
													exam.setDone(a.isDone());
													exam.setImportant(a
															.isImportant());
													exam.setId(a.getId());
													examsDb.save(exam);
												}
												list_exam = examsDb
														.findAll(Exam.class);
												list1 = new ArrayList<>();
												if (list_exam_new != null
														&& list_exam_new.size() > 0) {
													for (int i = 0; i < list_exam_new
															.size(); i++) {
														Map<String, Object> map = new ArrayMap<String, Object>();
														map.put("isDone",
																list_exam_new
																		.get(i)
																		.isDone());
														if (list_exam_new
																.get(i)
																.isDone()) {
															map.put("done",
																	R.drawable.done);
														} else {
															map.put("done",
																	R.drawable.undo);
														}
														map.put("mail", mail);
														map.put("examtime",
																list_exam_new
																		.get(i)
																		.getTime());
														map.put("examname",
																list_exam_new
																		.get(i)
																		.getName());
														map.put("isImportant",
																list_exam_new
																		.get(i)
																		.isImportant());
														if (list_exam_new
																.get(i)
																.isImportant()) {
															map.put("important",
																	R.drawable.important);
														} else {
															map.put("important",
																	R.drawable.unimportant);
														}
														list1.add(map);
													}
												} else {
													Map<String, Object> map = new ArrayMap<String, Object>();
													map.put("done",
															R.drawable.done);
													map.put("isDone", true);
													map.put("examname",
															"抱歉，账号无数据");
													map.put("examtime",
															"2014年6月27日");
													map.put("isImportant", true);
													map.put("important",
															R.drawable.important);
													list1.add(map);
												}
												String[] from1 = { "done",
														"examname", "examtime",
														"important" };
												int[] to1 = { R.id.done1,
														R.id.examname,
														R.id.examtime,
														R.id.important1 };
												examsAdapter = new SimpleAdapter(
														getActivity(), list1,
														R.layout.item_exam,
														from1, to1);
												examsPullToRefreshListView
														.setAdapter(examsAdapter);
												examsAdapter
														.notifyDataSetChanged();
												examsPullToRefreshListView
														.onRefreshComplete();

											} else {
												examRefresh();
												send();
											}
											if (a) {
												Toast.makeText(getActivity(),
														"最新数据",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(getActivity(),
														"刷新成功",
														Toast.LENGTH_SHORT)
														.show();
											}
										}

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Toast.makeText(getActivity(),
													"无网络连接", Toast.LENGTH_SHORT)
													.show();

											examRefresh();

										}

									});
						}
					});
		}
		examRefresh();

		examsPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						if (mail != null) {
							final TextView pn = (TextView) view
									.findViewById(R.id.examname);
							final TextView tv = (TextView) view
									.findViewById(R.id.examtime);
							LinearLayout ll = (LinearLayout) view
									.findViewById(R.id.item_exam);
							final String examtime = tv.getText().toString();
							final String examname = pn.getText().toString();

							final boolean checkname = "无数据".equals(examname)
									& "抱歉，账号无数据".equals(examname);
							isImportant = (boolean) list1.get(position - 1)
									.get("isImportant");
							isDone = (boolean) list1.get(position - 1).get(
									"isDone");

							final Exam exam = new Exam();
							exam.setMail(mail);
							exam.setName(examname);
							exam.setTime(examtime);
							exam.setImportant(isImportant);
							exam.setDone(isDone);

							ll.setOnLongClickListener(new OnLongClickListener() {

								@Override
								public boolean onLongClick(View v) {
									AlertDialog.Builder bulider = new Builder(
											getActivity());
									bulider.setTitle("是否删除!")
											.setNegativeButton(
													"刪除",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															examsDb.deleteByWhere(
																	Exam.class,
																	"name=\""
																			+ examname
																			+ "\"");
															Toast.makeText(
																	getActivity(),
																	"已刪除，请刷新数据!",
																	Toast.LENGTH_SHORT)
																	.show();

															send();
														}

													})
											.setPositiveButton("取消", null)
											.show();
									return true;
								}
							});

							final ImageView done1 = (ImageView) view
									.findViewById(R.id.done1);

							final ImageView important1 = (ImageView) view
									.findViewById(R.id.important1);

							done1.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									if (isDone) {
										done1.setImageResource(R.drawable.undo);
										isDone = !isDone;
										if (!checkname) {
											exam.setDone(isDone);
											examsDb.update(exam, "name=\""
													+ examname + "\"");
										}

										list1.get(position - 1).put("isDone",
												false);
									} else {
										done1.setImageResource(R.drawable.done);
										list1.get(position - 1).put("isDone",
												true);
										isDone = !isDone;
										if (!checkname) {
											exam.setDone(isDone);
											examsDb.update(exam, "name=\""
													+ examname + "\"");
										}
									}
								}
							});
							important1
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											if (isImportant) {
												important1
														.setImageResource(R.drawable.unimportant);
												isImportant = !isImportant;
												list1.get(position - 1).put(
														"isImportant", false);
												if (!checkname) {
													exam.setImportant(isImportant);
													examsDb.update(exam,
															"name=\""
																	+ examname
																	+ "\"");
												}
											} else {
												important1
														.setImageResource(R.drawable.important);
												list1.get(position - 1).put(
														"isImportant", true);
												isImportant = !isImportant;
												if (!checkname) {
													exam.setImportant(isImportant);
													examsDb.update(exam,
															"name=\""
																	+ examname
																	+ "\"");
												}
											}
										}
									});
						}
					}
				});
		return view;
	}

	/**
	 * 用户每日计划和考试提醒的上传 用户在当前界面的数据修改先保存在本地， 当用户离开当前的fragment时，进行数据上传
	 */
	@Override
	public void onDestroy() {
		send();
		super.onDestroy();
	}

	/**
	 * 发送本地“每日计划”和“考试提醒”到服务器进行备份
	 */
	private void send() {
		if (mail != null) {
			Gson gson = new Gson();
			AjaxParams params = new AjaxParams();
			list_plan = plansDb.findAll(Plan.class);
			list_exam = examsDb.findAll(Exam.class);
			try {
				if (list_plan.size() > 0) {
					String plangson = gson.toJson(list_plan);
					params.put("plangson", URLEncoder.encode(plangson, "utf-8"));
				} else {
					params.put("plangson", URLEncoder.encode("null", "utf-8"));
				}
				if (list_exam.size() > 0) {
					String examgson = gson.toJson(list_exam);
					params.put("examgson", URLEncoder.encode(examgson, "utf-8"));
				} else {
					params.put("examgson", URLEncoder.encode("null", "utf-8"));
				}
				FinalHttp fh = new FinalHttp();
				fh.post(NetCantants.SAVE_DAY_PLAN_SERVLET_URL, params,
						new AjaxCallBack<Object>() {
							@Override
							public void onSuccess(Object t) {
								super.onSuccess(t);
							}
						});
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每日计划的本地数据库的显示 无网络连接时，已经初始化时使用
	 */
	private void planRefresh() {

		list_plan = plansDb.findAll(Plan.class);
		list = new ArrayList<>();
		if (list_plan != null && list_plan.size() > 0 && mail != null) {

			for (int i = 0; i < list_plan.size(); i++) {
				Map<String, Object> map = new ArrayMap<String, Object>();
				map.put("isDone", list_plan.get(i).isDone());
				if (list_plan.get(i).isDone()) {
					map.put("done", R.drawable.done);
				} else {
					map.put("done", R.drawable.undo);
				}
				map.put("mail", mail);
				map.put("planname", list_plan.get(i).getPlanName());
				map.put("isImportant", list_plan.get(i).isImportant());
				if (list_plan.get(i).isImportant()) {
					map.put("important", R.drawable.important);
				} else {
					map.put("important", R.drawable.unimportant);
				}
				list.add(map);
			}
		} else {
			Map<String, Object> map = new ArrayMap<String, Object>();
			map.put("done", R.drawable.done);
			map.put("isDone", false);
			map.put("planname", "无数据");
			map.put("isImportant", true);
			map.put("important", R.drawable.important);
			list.add(map);
		}
		String[] from = { "done", "planname", "important" };
		int[] to = { R.id.done, R.id.planname, R.id.important };
		plansAdapter = new SimpleAdapter(getActivity(), list,
				R.layout.item_plan, from, to);
		plansPullToRefreshListView.setAdapter(plansAdapter);
		plansAdapter.notifyDataSetChanged();
		plansPullToRefreshListView.onRefreshComplete();
	}

	/**
	 * 考試提醒的本地数据库的显示 无网络连接时，已经初始化时使用
	 */

	private void examRefresh() {

		list_exam = examsDb.findAll(Exam.class);
		list1 = new ArrayList<>();
		if (list_exam != null && list_exam.size() > 0 && mail != null) {
			for (int i = 0; i < list_exam.size(); i++) {
				Map<String, Object> map = new ArrayMap<String, Object>();
				map.put("isDone", list_exam.get(i).isDone());
				if (list_exam.get(i).isDone()) {
					map.put("done", R.drawable.done);
				} else {
					map.put("done", R.drawable.undo);
				}
				map.put("mail", mail);
				map.put("examtime", list_exam.get(i).getTime());
				map.put("examname", list_exam.get(i).getName());
				map.put("isImportant", list_exam.get(i).isImportant());
				if (list_exam.get(i).isImportant()) {
					map.put("important", R.drawable.important);
				} else {
					map.put("important", R.drawable.unimportant);
				}
				list1.add(map);
			}

		} else {
			Map<String, Object> map = new ArrayMap<String, Object>();
			map.put("done", R.drawable.done);
			map.put("isDone", true);
			map.put("examname", "无数据");
			map.put("examtime", "2014年6月27日");
			map.put("isImportant", true);
			map.put("important", R.drawable.important);
			list1.add(map);
		}
		String[] from1 = { "done", "examname", "examtime", "important" };
		int[] to1 = { R.id.done1, R.id.examname, R.id.examtime, R.id.important1 };
		examsAdapter = new SimpleAdapter(getActivity(), list1,
				R.layout.item_exam, from1, to1);
		examsPullToRefreshListView.setAdapter(examsAdapter);
		examsAdapter.notifyDataSetChanged();
		examsPullToRefreshListView.onRefreshComplete();
	}
}
