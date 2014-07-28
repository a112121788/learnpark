package net.learnpark.app.learnpark.fragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.learnpark.app.learnpark.R;
import net.learnpark.app.learnpark.VideoPlayActivity;
import net.learnpark.app.learnpark.entity.File;
import net.learnpark.app.learnpark.net.LocalCantants;
import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.FileUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 在主界面点网络课程
 * 
 * @author peng
 * 
 */
public class NetClassFragment extends Fragment {
	TabHost tabHost;
	SharedPreferences sp;
	String username;
	ProgressBar download_bar;
	LinkedList<Map<String, Object>> data;
	SimpleAdapter netclassAdapter;
	LinkedList<Map<String, Object>> data_local;
	ListView local_lv;
	SimpleAdapter localAdapter;
	List<String> data_localname;
	TextView filesize;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_netclass, container,
				false);
		// 在线课程和已经下载的课程
		sp = getActivity()
				.getSharedPreferences("setting", Context.MODE_PRIVATE);
		username = sp.getString("username", "");
		// 在线课程
		tabHost = (TabHost) view.findViewById(R.id.all_columns);
		tabHost.setup();
		TabSpec my_tab1 = tabHost.newTabSpec("netclass").setIndicator("在线课堂")
				.setContent(R.id.netclass_box);
		// 已经下载的课程
		tabHost.addTab(my_tab1);
		TabSpec my_tab2 = tabHost.newTabSpec("local").setIndicator("我的下载")
				.setContent(R.id.local_box);
		tabHost.addTab(my_tab2);
		ListView netclass_lv = (ListView) view.findViewById(R.id.netclass);

		// 准备网络资源的的初始化工作

		data = new LinkedList<Map<String, Object>>();
		int[] to = { R.id.fileimg, R.id.filename, R.id.filetime, R.id.filewho };
		String[] from = { "fileimg", "filename", "filetime", "filewho" };
		netclassAdapter = new SimpleAdapter(getActivity(), data,
				R.layout.item_file, from, to);
		netclass_lv.setAdapter(netclassAdapter);

		// 准备网络资源的的数据
		username = sp.getString("username", "");
		if (username.equals("")) {
			Toast.makeText(getActivity(), "你还未登陆无法查看最新资源", 2).show();
			return view;
		}
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();
		try {
			params.put("username", URLEncoder.encode(username, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		fh.post(NetCantants.GET_File_COMMON_SERVLET_URL, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {

						if (t != null) {
							if ("false".equals(t)) {

								Toast.makeText(getActivity(), "无数据", 1).show();
							} else {

								// 查看最新资源
								Gson g = new Gson();
								Type typeOfT = new TypeToken<List<File>>() {
								}.getType();
								List<File> fileList = g.fromJson((String) t,
										typeOfT);
								if (fileList != null) {
									for (int i = 0; i < fileList.size(); i++) {
										Map<String, Object> map = new ArrayMap<String, Object>();
										map.put("filename", fileList.get(i)
												.getName());
										map.put("fileimg",
												getFileTypeIcon(fileList.get(i)
														.getUrl()));
										map.put("url", "  "
												+ fileList.get(i).getUrl());
										map.put("filewho", "发布者:"
												+ fileList.get(i).getWhoname());
										map.put("filetime",
												getTimeStrs(fileList.get(i)
														.getTime().getTime()));
										map.put("type", fileList.get(i)
												.getType());
										map.put("url", fileList.get(i).getUrl());
										if (!data.contains(map)) {
											data.addFirst(map);
										}
										netclassAdapter.notifyDataSetChanged();
									}
								}
							}
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(getActivity(), "请打开网络",
								Toast.LENGTH_SHORT).show();
					}
				});

		// 准备本地资源
		data_localname = FileUtil.findFiles("/mnt/sdcard/learnpark/file");
		// 我的下载
		local_lv = (ListView) view.findViewById(R.id.local);
		data_local = new LinkedList<Map<String, Object>>();
		int[] to1 = { R.id.fileimg, R.id.filename, R.id.url };
		String[] from1 = { "fileimg", "filename", "url" };
		if (data_local != null) {
			for (int i = 0; i < data_localname.size(); i++) {
				Map<String, Object> map = new ArrayMap<String, Object>();
				map.put("fileimg", getFileTypeIcon(data_localname.get(i)));
				map.put("filename",
						data_localname.get(i).substring(
								data_localname.get(i).lastIndexOf("/") + 1,
								data_localname.get(i).lastIndexOf(".")));
				map.put("url", data_localname.get(i));
				data_local.add(map);
			}
		}
		localAdapter = new SimpleAdapter(getActivity(), data_local,
				R.layout.item_localfile, from, to);
		local_lv.setAdapter(localAdapter);

		// 网络资源的子菜单监听
		netclass_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				new AlertDialog.Builder(getActivity())
						.setTitle(data.get(position).get("filename") + "")
						.setPositiveButton("下载",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										// 开始下载
										FinalHttp fh1 = new FinalHttp();
										String targettype = (String) data.get(
												position).get("url");
										String target = LocalCantants.FILE
												+ data.get(position).get(
														"filename")
												+ targettype.substring(targettype
														.lastIndexOf("."));
										filesize = (TextView) view
												.findViewById(R.id.filesize);

										download_bar = (ProgressBar) view
												.findViewById(R.id.download_bar);
										fh1.download(
												NetCantants.NET_BASE
														+ data.get(position)
																.get("url")
														+ "",
												target,
												new AjaxCallBack<java.io.File>() {
													long precurrent = 0L;

													@Override
													public void onStart() {
														download_bar
																.setVisibility(View.VISIBLE);
														filesize.setVisibility(View.VISIBLE);
													}

													@Override
													public void onLoading(
															long count,
															long current) {
														download_bar
																.setProgress((int) (100.0f * current / count));
														String downspeend = ""
																+ (int) ((current - precurrent) / 1024f)
																+ "k/s "
																+ (int) (current / 1024f / 1024f)
																+ "M/"
																+ (int) (count / 1024f / 1024f)
																+ "M";

														filesize.setText(downspeend);
														precurrent = current;
													}

													@Override
													public void onSuccess(
															java.io.File t) {
														super.onSuccess(t);
														filesize.setVisibility(View.GONE);
														download_bar
																.setVisibility(View.GONE);
													}

													@Override
													public void onFailure(
															Throwable t,
															int errorNo,
															String strMsg) {
														Toast.makeText(
																getActivity(),
																"下载失败，请检查网络", 2)
																.show();
														filesize.setVisibility(View.GONE);
														download_bar
																.setVisibility(View.GONE);
													}
												});
									}
								})
						.setNeutralButton("播放",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 调到播放界面
										dialog.dismiss();
										Intent intent = new Intent(
												getActivity(),
												VideoPlayActivity.class);
										intent.putExtra("url",
												NetCantants.NET_BASE
														+ data.get(position)
																.get("url"));
										Toast.makeText(
												getActivity(),
												NetCantants.NET_BASE
														+ data.get(position)
																.get("url"), 2)
												.show();
										startActivity(intent);

									}
								}).setNegativeButton("取消", null).show();
			}
		});

		// 本地资源监听
		local_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getActivity()).setTitle("本地资源")
				// .setPositiveButton("分享",
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// dialog.dismiss();
				// // 开始下载
				// }
				// })
						.setNeutralButton("打开",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										// 调到播放界面
										Intent intent = new Intent(
												getActivity(),
												VideoPlayActivity.class);
										intent.putExtra(
												"url",
												""
														+ data_local.get(
																position).get(
																"url"));
										startActivity(intent);

									}
								}).setNegativeButton("取消", null).show();
			}
		});
		// tab的监听
		// tab的监听
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("local")) {
					List<String> data_localtemp = FileUtil
							.findFiles("/mnt/sdcard/learnpark/file");
					if (data_local != null) {
						for (int i = 0; i < data_localtemp.size(); i++) {
							Map<String, Object> map = new ArrayMap<String, Object>();
							map.put("fileimg",
									getFileTypeIcon(data_localtemp.get(i)));
							map.put("filename",
									data_localtemp.get(i).substring(
											data_localtemp.get(i).lastIndexOf(
													"/") + 1,
											data_localtemp.get(i).lastIndexOf(
													".")));
							map.put("url", data_localtemp.get(i));
							if (!data_local.contains(map)) {
								data_local.add(map);
							}
						}
						localAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		return view;
	}

	private String getTimeStrs(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		long msgtime = calendar.getTimeInMillis();
		long currenttime = System.currentTimeMillis();
		long timeDiata = currenttime - msgtime;

		if (timeDiata > 24 * 60 * 60 * 1000) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String time = (month + 1) + "月" + day + "日";
			return "上传时间：" + time;
		} else {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			String time = (hour > 9 ? hour : "0" + hour) + ":"
					+ (minute > 9 ? minute : "0" + minute);
			return "上传时间：" + time;
		}

	}

	private int getFileTypeIcon(String filepath) {
		if (filepath != null) {
			if (!filepath.equals("")) {
				String str = filepath.substring(filepath.lastIndexOf("."));
				switch (str) {

				case ".avi":
					return R.drawable.icon_avi_72;
				case ".wmv":
					return R.drawable.icon_wmv_72;
				case ".mp4":
					return R.drawable.icon_mp4_72;
				case ".3gp":
					return R.drawable.icon_mp4_72;
				case ".rmvb":
					return R.drawable.icon_mp4_72;
				case ".mp3":
					return R.drawable.icon_mp3_72;
				case ".doc":
					return R.drawable.icon_doc_72;
				case ".flv":
					return R.drawable.icon_flv_72;
				case ".jpg":
					return R.drawable.icon_jpg_72;
				case ".pdf":
					return R.drawable.icon_pdf_72;
				case ".png":
					return R.drawable.icon_png_72;
				case ".rm":
					return R.drawable.icon_rm_72;
				case ".txt":
					return R.drawable.icon_txt_72;
				case ".xls":
					return R.drawable.icon_xls_72;
				default:
					break;
				}
			}
		}
		return R.drawable.icon_none_72;
	}

}
