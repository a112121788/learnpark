package net.learnpark.app.learnpark.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.util.ArrayMap;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

/**
 * 设备相关信息工具类
 * @author peng
 * 
 */
public class DeviceInfoUtil {
	/**
	 * 获取android当前可用内存大小
	 * 
	 * @param context
	 * @return 规格化 的内存
	 */
	public static String getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 获得系统总内存
	 * 
	 * @param context
	 * @return
	 */

	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获得手机屏幕宽高
	 * 
	 * @param activity
	 * @return
	 */
	public static int getHeight(Activity activity) {
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		return height;
	}

	public static int getWidth(Activity activity) {
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * ，号，手机型号,手机品牌
	 * 
	 * @param activity
	 * @return
	 */
	/**
	 * 获取IMEI号
	 * @param activity
	 * @return
	 */
	public static String getIMEI(Activity activity) {
		TelephonyManager mTm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		return imei;
	}
	/**
	 *  获取IESI号 手机型号
	 * @param activity
	 * @return
	 */
	public static String getIESI(Activity activity) {
		TelephonyManager mTm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTm.getSubscriberId();
		return imsi;
	}
	/**
	 * 获取手机型号
	 * @param activity
	 * @return
	 */
	public static String getPhoneType(Activity activity) {
		TelephonyManager mTm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		return  android.os.Build.MODEL;
	}
	/**
	 * 获取 手机品牌
	 * @param activity
	 * @return
	 */
	public static String getPhoneBrand(Activity activity) {
		TelephonyManager mTm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		return   android.os.Build.BRAND;
	}
	/**
	 * 获取 手机号码 有时候获取不到
	 * @param activity
	 * @return
	 */
	public static String getPhonNumber(Activity activity) {
		TelephonyManager mTm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = mTm.getLine1Number();
		return   number;
	}

	/**
	 * 获取手机MAC地址 只有手机开启wifi才能获取到mac地址
	 * @param activity
	 * @return
	 */
	public static String getMacAddress(Activity activity) {
		String result = "";
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		Log.i("text", "手机macAdd:" + result);
		return result;
	}

	/**
	 * 手机CPU信息
	 */
	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
		return cpuInfo;
	}
}
