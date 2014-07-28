package net.learnpark.app.learnpark;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.learnpark.app.learnpark.entity.User;
import net.learnpark.app.learnpark.net.NetCantants;
import net.learnpark.app.learnpark.util.ImageUtil;
import net.learnpark.app.learnpark.util.SDCardsUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * 用户个人中心 上传的照片名称还需要修改
 * 
 * @author peng
 * @version 1 2014-6-12 14:12:46
 */
public class UserCenterActivity extends ActionBarActivity {

	TextView user_name;
	TextView user_mail;
	TextView user_school;
	TextView user_number;
	TextView user_grade;
	TextView user_major;
	TextView user_wechat;
	TextView user_userclass;
	ImageView user_photo;
	String photoname = null;
	String timename = null;// 头像的名称
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);

		// 统一ActionBar的背景 by 高帅朋
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("个人中心");
		SysApplication.getInstance().addActivity(this); 
		
		user_photo = (ImageView) findViewById(R.id.user_photo);
		user_name = (TextView) findViewById(R.id.user_name);
		user_mail = (TextView) findViewById(R.id.user_mail);
		user_school = (TextView) findViewById(R.id.user_school);
		user_number = (TextView) findViewById(R.id.user_number);
		user_grade = (TextView) findViewById(R.id.user_grade);
		user_major = (TextView) findViewById(R.id.user_major);
		user_userclass = (TextView) findViewById(R.id.user_userclass);
		user_wechat = (TextView) findViewById(R.id.user_wechat);
		if (BitmapFactory.decodeFile("/mnt/sdcard/learnpark/user/user.png") != null) {
			user_photo.setImageBitmap(BitmapFactory
					.decodeFile("/mnt/sdcard/learnpark/user/user.png"));
		}
		final String user = user_mail.getText().toString();
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		final String username = sp.getString("username", "");
		final String password = sp.getString("password", "");

		AjaxParams getuserinfo_params = new AjaxParams();
		try {
			getuserinfo_params.put("username",
					URLEncoder.encode(username, "utf-8"));
			getuserinfo_params.put("password",
					URLEncoder.encode(password, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// 网络 验证用户信息
		FinalHttp fh = new FinalHttp();
		fh.post(NetCantants.GET_USER_INFO_URL, getuserinfo_params,
				new AjaxCallBack<Object>() {
					@Override
					public void onStart() {
					};

					public void onLoading(long count, long current) {
					}

					@Override
					public void onSuccess(Object t) {

						if (t != null) {
							if ("error".equals(t)) {

							} else {
								Gson gson = new Gson();
								User user = gson.fromJson((String) t,
										User.class);
								if (user != null) {
									user_name.setText(user.getName());
									user_mail.setText(user.getUsername());
									user_number.setText(user.getNumber());
									user_school.setText(user.getSchool());
									user_grade.setText(user.getGrade());
									user_major.setText(user.getMajor());
									user_userclass.setText(user.getUserclass());

									SharedPreferences.Editor editor = sp.edit();
									editor.putString("name", user.getName());
									editor.putString("user_school",
											user.getSchool());
									editor.putString("username",
											user.getUsername());
									editor.putString("user_number",
											user.getNumber());
									editor.putString("user_mail",
											user.getUsername());
									editor.putString("user_grade",
											user.getGrade());
									editor.putString("user_major",
											user.getMajor());
									editor.putString("user_userclass",
											user.getUserclass());
									editor.commit();
								}
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						user_name.setText(sp.getString("name", ""));
						user_mail.setText(sp.getString("user_mail", ""));
						user_mail.setText(sp.getString("username", ""));
						user_number.setText(sp.getString("user_number", ""));
						user_school.setText(sp.getString("user_school", ""));
						user_grade.setText(sp.getString("user_grade", ""));
						user_major.setText(sp.getString("user_major", ""));
						user_userclass.setText(sp.getString("user_userclass",
								""));
					}
				});

		findViewById(R.id.user_center_photo).setOnLongClickListener(
				new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						Toast.makeText(UserCenterActivity.this, "长按修改头像",
								Toast.LENGTH_LONG).show();

						AlertDialog.Builder bulider = new Builder(
								UserCenterActivity.this);
						bulider.setTitle("设置头像")
								.setNegativeButton("相册",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												timename = getPhotoFileName();
												photoname = File.separator
														+ timename;
												Intent intent = new Intent(
														Intent.ACTION_PICK,
														null);
												intent.setDataAndType(
														MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
														"image/*");
												startActivityForResult(intent,
														1);
											}
										})
								.setPositiveButton("拍照",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												timename = getPhotoFileName();
												photoname = File.separator
														+ timename;
												Bundle bundle = new Bundle();
												Intent intent = new Intent(
														MediaStore.ACTION_IMAGE_CAPTURE);
												intent.putExtra(
														MediaStore.EXTRA_OUTPUT,
														Uri.fromFile(new File(
																Environment
																		.getExternalStorageDirectory(),
																timename)));
												intent.putExtras(bundle);
												startActivityForResult(intent,
														2);
											}
										}).show();
						return true;
					}
				});
		// 用户点击注销按钮
		findViewById(R.id.user_logout).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(UserCenterActivity.this)
								.setTitle("确定注销？")
								.setPositiveButton("是的",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												// 清空本地信息
												SharedPreferences.Editor editor = sp
														.edit();
												editor.putString("name", "");
												editor.putString("username", "");
												editor.putString("password", "");
												editor.putString("user_name",
														"");
												editor.putString("user_school",
														"");
												editor.putString("user_mail",
														"");
												editor.putString("user_number",
														"");
												editor.putString("user_grade",
														"");
												editor.putString("user_major",
														"");
												editor.putString(
														"user_userclass", "");
												editor.commit();
												// 跳转到主界面
												startActivity(new Intent(
														getApplicationContext(),
														MainActivity.class));
												finish();
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null).show();

					}
				});
		user_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						WechatBindActivity.class));
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case 1:
				if (data.getData() != null) {
					startPhotoZoom(data.getData());
				}
				break;
			case 2:
				if (SDCardsUtil.hasSdcard()) {
					File temp = new File(
							Environment.getExternalStorageDirectory()
									+ photoname);
					startPhotoZoom(Uri.fromFile(temp));
				} else {
					Toast.makeText(UserCenterActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 3:
				if (data != null) {
					setPicToView(data);
				}
				break;
			default:
				break;
			}
		}
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			if (photo != null) {
				user_photo.setImageBitmap(photo);
				if (ImageUtil.saveCompressPicLocal(photo,
						"mnt/sdcard/learnpark/user", "user.png")) {
					Toast.makeText(getApplicationContext(),
							"本地保存成功 在mnt/sdcard/learnpark/user/文件夹中", 2).show();
				}
				// 上传到网上
				ImageUtil.saveCompressPicToServer(NetCantants.SAVE_PHOTEO_URL,
						user_mail.getText().toString(),
						sp.getString("password", ""), photo);
			}
		}
	}

	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.usercenter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.usercenter_edit) {

			Intent intent = new Intent(getApplicationContext(),
					UserCenterEditActivity.class);
			startActivity(intent);
		}
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

}
