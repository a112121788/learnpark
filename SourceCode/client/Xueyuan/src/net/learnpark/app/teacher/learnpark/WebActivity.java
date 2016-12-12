package net.learnpark.app.teacher.learnpark;

import net.learnpark.app.teacher.learnpark.shixian.SysApplication;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends ActionBarActivity {
	WebView wView1;
	ProgressBar web_progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		// actionbar的颜色
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// 设置退出
		SysApplication.getInstance().addActivity(this);

		wView1 = (WebView) findViewById(R.id.webview1);
		web_progressbar = (ProgressBar) findViewById(R.id.web_progressbar);

		// 设置支持JavaScript脚本
		WebSettings webSettings = wView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAppCachePath("/mnt/sdcard/learnpark/");
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 支持缩放
		webSettings.setSupportZoom(true);
		// 缩放按钮
		webSettings.setBuiltInZoomControls(true);
		wView1.loadUrl("");
		// 设置webview 不让启动系统的浏览器
		wView1.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.endsWith(".mp4")) {
					Intent intent = new Intent("android.intent.action.VIEW",
							Uri.parse(url));
					startActivity(intent);
					return true;
				} else {
					view.loadUrl(url);
					return super.shouldOverrideUrlLoading(view, url);
				}
				// if (url.endsWith(".mp4"){
				// Intent intent = new Intent("android.intent.action.VIEW",
				// Uri.parse(url));
				// view.getContext().startActivity(intent);
				// return true;
				// } else {
				// return super.shouldOverrideUrlLoading(view, url);
				// }

			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				web_progressbar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				web_progressbar.setVisibility(View.INVISIBLE);
			}

		});
	}

	@Override
	public void onBackPressed() {
		wView1.goBack();
	}
}