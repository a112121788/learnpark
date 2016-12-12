package net.learnpark.app.learnpark.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.ViewConfiguration;

public class Setting {

	/**
	 * 让带实体键的手机也显示ActionBar
	 * 
	 * @author peng
	 * @version 1 2014年6月12日 09:11:14
	 */
	public static void getOverflowMenu(Context context) {

		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}