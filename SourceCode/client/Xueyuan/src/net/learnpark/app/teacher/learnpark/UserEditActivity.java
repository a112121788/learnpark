package net.learnpark.app.teacher.learnpark;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.params.CoreConnectionPNames;

import net.learnpark.app.teacher.learnpark.net.NetCantants;
import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserEditActivity extends ActionBarActivity implements
		OnClickListener {

	private String filepath = Environment.getExternalStorageDirectory()
			.getPath() + "/learnpark/user/user.png";

	private String sp_user_name;
	private String sp_user_school;
	private String sp_user_department;
	private String sp_user_mail;

	private TextView user_name;
	private TextView user_mail;
	private TextView user_school;
	private TextView user_department;
	private ImageView user_photo;
	private String photoname = null;
	private String timename = null;// 头像的名称
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		user_photo = (ImageView) findViewById(R.id.user_photo);
		// tv1 = (TextView) findViewById(R.id.user_name);
		user_name = (TextView) findViewById(R.id.user_name);
		user_mail = (TextView) findViewById(R.id.user_mail);
		user_school = (TextView) findViewById(R.id.user_school);
		user_department = (TextView) findViewById(R.id.user_department);

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		if (BitmapFactory.decodeFile(filepath) != null) {
			user_photo.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		sp_user_name = sp.getString("user_name", "");
		sp_user_mail = sp.getString("user_mail", "");
		sp_user_school = sp.getString("user_school", "");
		sp_user_department = sp.getString("user_department", "");

		// 将更改之前的信息显示到输入框内
		user_name.setText(sp_user_name);
		user_mail.setText(sp.getString("user_mail", "") + "  (邮箱暂不支持修改)");
		user_department.setText(sp_user_department);
		user_school.setText(sp_user_school);

		// 设置头像点击监听事件
		user_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(UserEditActivity.this, "头像", Toast.LENGTH_SHORT)
						.show();

				AlertDialog.Builder bulider = new Builder(UserEditActivity.this);
				bulider.setTitle("设置头像")
						.setNegativeButton("相册",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										timename = ImageUtil.getPhotoFileName();
										photoname = File.separator + timename;
										Intent intent = new Intent(
												Intent.ACTION_PICK, null);
										intent.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
										startActivityForResult(intent, 1);
									}
								})
						.setPositiveButton("拍照",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										timename = ImageUtil.getPhotoFileName();
										photoname = File.separator + timename;
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
										startActivityForResult(intent, 2);
									}
								}).show();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem menuItem = menu.add(0, 0, 0, "提交修改");
		MenuItemCompat.setShowAsAction(menuItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 0) {
			Log.d("TAG", sp_user_name + "555" + sp_user_school + 555
					+ sp_user_department + "  " + 88);
			Log.d("TAG", user_name.getText().toString() + "555"
					+ user_school.getText().toString() + 555
					+ user_department.getText().toString() + "  " + 88);
			if (user_name.getText().toString().equals(sp_user_name)
					&& user_school.getText().toString().equals(sp_user_school)
					&& user_department.getText().toString()
							.equals(sp_user_department)) {
				Toast.makeText(UserEditActivity.this, "未修改信息，无需提交",
						Toast.LENGTH_SHORT).show();
			} else {

				// 说明用户修改了信息，需要上传到服务器了

				AjaxParams modifyusermessages_params = new AjaxParams();
				try {
					modifyusermessages_params.put("user_mail",
							URLEncoder.encode(sp_user_mail, "utf-8"));
					modifyusermessages_params.put("user_name", URLEncoder
							.encode(user_name.getText().toString(), "utf-8"));
					modifyusermessages_params.put("user_school", URLEncoder
							.encode(user_school.getText().toString(), "utf-8"));
					modifyusermessages_params.put("user_department", URLEncoder
							.encode(user_department.getText().toString(),
									"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				FinalHttp fh = new FinalHttp();
				// 请求超时
				fh.getHttpClient()
						.getParams()
						.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
								5000);
				// 读取超时
				fh.getHttpClient().getParams()
						.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
				fh.post(NetCantants.MODIFY_USER_MESSAGES_URL,
						modifyusermessages_params, new AjaxCallBack<Object>() {
							@Override
							public void onStart() {
							};

							public void onLoading(long count, long current) {
							}

							@Override
							public void onSuccess(Object t) {

								if (t != null) {
									if ("true".equals(t)) {
										Toast.makeText(UserEditActivity.this,
												"修改成功", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(
												UserEditActivity.this,
												UserActivity.class);
										startActivity(intent);
										finish();
									} else {
										Toast.makeText(UserEditActivity.this,
												"修改失败", Toast.LENGTH_SHORT)
												.show();
									}
								}
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								Toast.makeText(UserEditActivity.this,
										"网络连接异常，修改失败，请重试", Toast.LENGTH_SHORT)
										.show();
							}
						});
			}
		}
		return super.onOptionsItemSelected(item);
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
					Toast.makeText(UserEditActivity.this, "未找到存储卡，无法存储照片！",
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

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
}
