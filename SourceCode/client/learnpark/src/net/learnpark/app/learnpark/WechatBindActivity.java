package net.learnpark.app.learnpark;

import net.learnpark.app.learnpark.util.ImageUtil;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

/**
 * 如何绑定微信
 * 
 * @author peng
 * @version 1 2014-6-12 14:12:46
 */
public class WechatBindActivity extends ActionBarActivity {
	ImageView wechat_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wechat_bind);

		// 统一ActionBar的背景 by 高帅朋
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setTitle("绑定微信");
		SysApplication.getInstance().addActivity(this);
		ImageUtil
				.saveCompressPicLocal(BitmapFactory.decodeResource(
						getResources(), R.drawable.weixin),
						"/mnt/sdcard/learnpark", "wenxin.png");

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
