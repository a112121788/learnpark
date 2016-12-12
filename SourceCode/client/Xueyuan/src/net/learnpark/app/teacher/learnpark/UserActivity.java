package net.learnpark.app.teacher.learnpark;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.params.CoreConnectionPNames;

import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import net.learnpark.app.teacher.learnpark.shixian.User;
import net.learnpark.app.teacher.learnpark.util.ImageUtil;
import net.learnpark.app.teacher.learnpark.util.SDCardsUtil;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class UserActivity extends ActionBarActivity {

	private TextView user_name;
	private TextView user_mail;
	private TextView user_school;
	private TextView user_department;
	private Button user_login;

	private String filepath = Environment.getExternalStorageDirectory()
			.getPath() + "/learnpark/user/user.png";
	// private TextView tv5;
	private ImageView user_photo;
	private String photoname = null;
	private String timename = null;// 头像的名称
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_user);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		// 统一ActionBar的背景 by 高帅朋
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		user_photo = (ImageView) findViewById(R.id.user_photo);
		// tv1 = (TextView) findViewById(R.id.user_name);
		user_name = (TextView) findViewById(R.id.user_name);
		user_mail = (TextView) findViewById(R.id.user_mail);
		user_school = (TextView) findViewById(R.id.user_school);
		user_department = (TextView) findViewById(R.id.user_department);
		user_login = (Button) findViewById(R.id.user_logout);
		if (BitmapFactory.decodeFile(filepath) != null) {
			user_photo.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}
		// final String user = user_mail.getText().toString();
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		final String username = sp.getString("user_mail", "");
		final String password = sp.getString("password", "");

		// 如果没有登陆过，就跳转到登陆界面
		if (username.equals("")) {
			setResult(RESULT_OK, (new Intent()).setAction(1 + ""));
			Intent intent = new Intent(UserActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		} else {
			String sp_user_name = sp.getString("user_name", "");
			String sp_user_school = sp.getString("user_school", "");
			String sp_user_department = sp.getString("user_department", "");
			user_name.setText(sp_user_name);
			user_mail.setText(username);
			user_school.setText(sp_user_school);
			user_department.setText(sp_user_department);
		}

		if (username.equals("") || password.equals("")) {

		} else {

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
			// 请求超时
			fh.getHttpClient()
					.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			fh.getHttpClient().getParams()
					.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
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
										user_school.setText(user.getSchool());
										user_department.setText(user
												.getDepartment());

										SharedPreferences.Editor editor = sp
												.edit();
										editor.putString("user_name",
												user.getName());
										editor.putString("user_school",
												user.getSchool());
										editor.putString("user_mail",
												user.getUsername());
										editor.putString("user_department",
												user.getDepartment());
										editor.commit();
									}
								}
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// user_name.setText(sp.getString("user_name", ""));
							// user_mail.setText(sp.getString("user_mail", ""));
							// user_school.setText(sp.getString("user_school",
							// ""));
							// user_department.setText(sp.getString("user_department",
							// ""));
						}
					});
		}
		findViewById(R.id.user_center_photo).setOnLongClickListener(
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {

						Toast.makeText(UserActivity.this, "头像",
								Toast.LENGTH_SHORT).show();

						AlertDialog.Builder bulider = new Builder(
								UserActivity.this);
						bulider.setTitle("设置头像")
								.setNegativeButton("相册",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												timename = ImageUtil
														.getPhotoFileName();
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
												timename = ImageUtil
														.getPhotoFileName();
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
		user_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog;
				AlertDialog.Builder bulider = new Builder(UserActivity.this);
				bulider.setIcon(R.drawable.ic_launcher);
				bulider.setTitle("是否注销？");
				bulider.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								setResult(RESULT_OK, (new Intent()).setAction(1 + ""));
								
								// 清空本地信息
								SharedPreferences.Editor editor = sp.edit();
								editor.putString("user_mail", "");
								editor.putString("password", "");
								editor.putString("user_name", "");
								editor.putString("user_school", "");
								editor.putString("user_department", "");
								editor.commit();
								// 跳转到登陆界面
								Intent intent = new Intent(UserActivity.this,
										LoginActivity.class);
								// Bundle bundle = new Bundle();
								// bundle.putString("uid",
								// UserActivity.this.toString());
								// intent.putExtras(bundle);
								startActivity(intent);
								finish();
							}
						});
				bulider.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						});
				alertDialog = bulider.create();
				alertDialog.show();
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
					Toast.makeText(UserActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_SHORT).show();
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
							"本地保存成功 在mnt/sdcard/learnpark/user/文件夹中",
							Toast.LENGTH_SHORT).show();
				}
				// 上传到网上
				ImageUtil.saveCompressPicToServer(NetCantants.SAVE_PHOTEO_URL,
						user_mail.getText().toString(),
						sp.getString("password", ""), photo);
			}
		}
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
		switch (item.getItemId()) {
		case R.id.usercenter_edit:

			if (user_mail.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "请先登陆",
						Toast.LENGTH_SHORT).show();
			} else {
				setResult(RESULT_OK, (new Intent()).setAction(1 + ""));
				Intent intent = new Intent(getApplicationContext(),
						UserEditActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		case android.R.id.home:
			setResult(RESULT_OK, (new Intent()).setAction(1 + ""));
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return true;
	}
}
