package net.learnpark.app.teacher.learnpark.util;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

/**
 * 
 * SDCard工具类
 * @author 陆礼祥
 * 
 */
public class SDCardsUtil {
	/**
	 * 判断时候有内存卡
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断内存卡的数量 0 代表没有 
	 * @param activity
	 * @return
	 */
	@SuppressLint("NewApi")
	public static int getSDcardNums(Activity activity) {
		StorageManager storageManager = (StorageManager) activity
				.getSystemService(Context.STORAGE_SERVICE);
		try {
			Class<?>[] paramClasses = {};
			Method getVolumePathsMethod = StorageManager.class.getMethod(
					"getVolumePaths", paramClasses);
			getVolumePathsMethod.setAccessible(true);
			Object[] params = {};
			Object invoke = getVolumePathsMethod.invoke(storageManager, params);
			switch (((String[]) invoke).length) {
			case 0:
				return 0;
			case 1:
				return 1;

			case 2:
				return 2;
			}

		} catch (Exception e) {
		}
		return 0;
	}
}
