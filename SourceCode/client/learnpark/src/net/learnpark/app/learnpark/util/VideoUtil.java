package net.learnpark.app.learnpark.util;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.widget.Toast;

/**
 * 与视频相关的工具类
 * 
 * @author peng
 * @version 1 2014年6月19日 19:05:08
 */
public class VideoUtil {

	/**
	 * 从视频中获取帧，并保存到本地
	 * 
	 * @@author peng
	 * @param url
	 *            视频的地址 本地或者网络上
	 * @param count
	 *            要保存帧的个数 帧间距为 视频的总长度/count
	 * @param where
	 *            要保存的地址 sdcard/where
	 * @param quality
	 *            图片质量 0~100 100表示不压缩
	 * @return true表示保存成功 false表示保存失败
	 */

	@SuppressLint("NewApi")
	public static boolean getFlamesFromVideo(String url, int count,
			String where, int quality) {
		String datapath = Environment.getExternalStorageDirectory() + url;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(datapath);
		// 获取视频的长度 单位为毫秒
		String time = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		// 获取视频的长度 单位为秒
		int seconds = Integer.valueOf(time) / 1000;

		// 得到图片
		int space = seconds / count;
		for (int i = 1; i <= seconds; i++) {
			Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000,
					MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
			System.out.println(bitmap.getHeight());
			String path = Environment.getExternalStorageDirectory()
					+ File.separator + where + File.separator + i + ".jpg";
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(path);
				bitmap.compress(CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@SuppressLint("NewApi")
	public static Bitmap getFlameFromVideoByTime(String url, String where,
			int quality, int videoTime) {
		//String datapath = Environment.getExternalStorageDirectory() + url;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(url);
		// 获取视频的长度 单位为毫秒
		String time = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		// 获取视频的长度 单位为秒
		int seconds = Integer.valueOf(time) / 1000;

		// 得到图片
		Bitmap bitmap = retriever.getFrameAtTime(videoTime*1000,
				MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
		System.out.println(bitmap.getHeight());
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + where + File.separator + videoTime + ".jpg";

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			bitmap.compress(CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			return bitmap;
		} catch (Exception e) {
			System.out.println("error bitmap");
		}
		return null;
	}
}
