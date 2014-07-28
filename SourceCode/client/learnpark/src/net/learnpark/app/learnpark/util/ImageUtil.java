package net.learnpark.app.learnpark.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.util.Base64;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 与图片相关的工具类
 * 
 * @author peng
 * 
 */
public class ImageUtil {
	/**
	 * 将截取的头像存储到本地
	 * 
	 * @param bitmap
	 * @param path
	 *            文件路径 demo /mnt/sdcard
	 * @param filename
	 *            demo a.png
	 * @return true 代表保存成功
	 */
	public static boolean saveCompressPicLocal(Bitmap bitmap, String path,
			String filename) {
		boolean isSave = false;// true 代表保存成功
		if (bitmap == null) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				FileOutputStream out = null;
				try {
					File f = new File(path + File.separator + filename);
					f.createNewFile();
					out = new FileOutputStream(f);
					if (bitmap != null) {
						isSave = bitmap.compress(Bitmap.CompressFormat.JPEG,
								100, out);
						out.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				// 文件夹创建失败
				return false;
			}
		} else {
			FileOutputStream out = null;
			try {
				File f = new File(path + File.separator + filename);
				f.createNewFile();
				out = new FileOutputStream(f);
				if (bitmap != null) {
					isSave = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							out);
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSave;
	}

	/**
	 * 将图片的字节流数据加密成base64字符输出
	 */
	public static void saveCompressPicToServer(String url, String username,
			String password, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		if (bitmap != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.close();
				byte[] buffer = baos.toByteArray();
				System.out.println("图片大小：" + buffer.length);
				String photo = Base64.encodeToString(buffer, 0, buffer.length,
						Base64.DEFAULT);
				RequestParams params = new RequestParams();
				params.put("photo", photo);
				params.put("photoname", getPhotoFileName());
				params.put("username", username);
				params.put("password", password);
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						// 头像上传成功
						try {
							throw new IOException();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable e, String data) {
						super.onFailure(e, data);
						// 头像上传失败
					}
				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
