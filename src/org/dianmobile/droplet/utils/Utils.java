package org.dianmobile.droplet.utils;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import org.dianmobile.droplet.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

/**
 * 常用工具类
 * 如检查网络、格式转换等
 * 
 * @author FreeTymeKiyan
 * @version 0.0.4
 */
public class Utils {
	
	/**应用存图片的相对目录*/
	public static final String HABIT_CHALLENGER_CACHE = "/HabitChallenger/cache/";
	/**人人头像缓存*/
	public static final String RENREN_CACHE = "/HabitChallenger/renrenFriendHead/";
	/**应用存图片的绝对目录*/
	private static String picDir = "";
	/**人人存头像的绝对目录*/
	private static String renrenHeadDir = "";

	/**
     * 检查网络连接的方法
     * 需要在AndroidManifest.xml中声明权限
     * 
     * @return 	false	未连接
     * 			true	连接
     */
    public static boolean checkNetworkState(Context context) {
		ConnectivityManager cm = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null)
		    return false; // 无法获取ConnectivityManager
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo == null) {
		    return false; // 无法获取NetworkInfo
		}
		if(netinfo.isConnected()) {
		    return true; // 已连接，包括通过电脑连接的方式
		}
		return false;
    }
    
    /** 
     * 获得一个UUID 
     * @return String UUID 
     */ 
    public static String getUuid(){ 
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }  
    
    /**
     * 得到sd卡的路径
     * 
     * @return String
     */
    public static String getSdPath() {
    	File SdDir = null;
    	boolean sdCardExist = Environment
    			.getExternalStorageState().equals
    			(android.os.Environment.MEDIA_MOUNTED);
    	if (sdCardExist) {
			SdDir = Environment.getExternalStorageDirectory();
		}
    	if (SdDir != null) {
			return SdDir.toString();
		} else {
			return null;
		}
	}
    
    /**
     * 创建sd卡上的目录
     * 
     * @param context
     */
    public static void createSdCardDir(Context context) {
    	if (getSdPath() == null) {
			Toast.makeText(context, R.string
					.toast_sdcard_not_found, 
					Toast.LENGTH_SHORT).show();
		} else {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				picDir = sdCardDir.getPath() + HABIT_CHALLENGER_CACHE;
				File rootPath = new File(picDir);
				if (!rootPath.exists()) {
					rootPath.mkdirs();
					System.out.println("path ok, path:" + picDir);
				}
			} else {
				System.out.println("sd card not mounted");
			}
		} 
	}

    /**
     * 返回应用存图片的绝对目录
     * 
     * @return String
     */
    public static String getPicDir() {
		return picDir;
	}
    
    /**
     * 返回应用存图片的绝对目录
     * 
     * @return String
     */
    public static String getRenrenCacheDir() {
		return renrenHeadDir;
	}
    
    /** 
	 * 格式化字符串(7:3->07:03) 
	 * */
	public static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/**
	 * 创建人人好友头像的缓存
	 * 
	 * @param Context
	 */
	public static void createRenrenCache(Context context) {
		if (getSdPath() == null) {
			Toast.makeText(context, R.string
					.toast_sdcard_not_found, 
					Toast.LENGTH_SHORT).show();
		} else {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				renrenHeadDir = sdCardDir.getPath() + RENREN_CACHE;
				File rootPath = new File(renrenHeadDir);
				if (!rootPath.exists()) {
					rootPath.mkdirs();
					System.out.println("path ok, path:" + renrenHeadDir);
				}
			} else {
				System.out.println("sd card not mounted");
			}
		}
	}
	
	/**
	 * 比较设定的时间和现在的时间
	 * 
	 * @return 	false	时间已过
	 * 			true	时间没过
	 */
	public static boolean compareTime(Calendar calendar) {
		if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}
}