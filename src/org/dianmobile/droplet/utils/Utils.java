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
 * ���ù�����
 * �������硢��ʽת����
 * 
 * @author FreeTymeKiyan
 * @version 0.0.4
 */
public class Utils {
	
	/**Ӧ�ô�ͼƬ�����Ŀ¼*/
	public static final String HABIT_CHALLENGER_CACHE = "/HabitChallenger/cache/";
	/**����ͷ�񻺴�*/
	public static final String RENREN_CACHE = "/HabitChallenger/renrenFriendHead/";
	/**Ӧ�ô�ͼƬ�ľ���Ŀ¼*/
	private static String picDir = "";
	/**���˴�ͷ��ľ���Ŀ¼*/
	private static String renrenHeadDir = "";

	/**
     * ����������ӵķ���
     * ��Ҫ��AndroidManifest.xml������Ȩ��
     * 
     * @return 	false	δ����
     * 			true	����
     */
    public static boolean checkNetworkState(Context context) {
		ConnectivityManager cm = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null)
		    return false; // �޷���ȡConnectivityManager
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo == null) {
		    return false; // �޷���ȡNetworkInfo
		}
		if(netinfo.isConnected()) {
		    return true; // �����ӣ�����ͨ���������ӵķ�ʽ
		}
		return false;
    }
    
    /** 
     * ���һ��UUID 
     * @return String UUID 
     */ 
    public static String getUuid(){ 
        String s = UUID.randomUUID().toString(); 
        //ȥ����-������ 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }  
    
    /**
     * �õ�sd����·��
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
     * ����sd���ϵ�Ŀ¼
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
     * ����Ӧ�ô�ͼƬ�ľ���Ŀ¼
     * 
     * @return String
     */
    public static String getPicDir() {
		return picDir;
	}
    
    /**
     * ����Ӧ�ô�ͼƬ�ľ���Ŀ¼
     * 
     * @return String
     */
    public static String getRenrenCacheDir() {
		return renrenHeadDir;
	}
    
    /** 
	 * ��ʽ���ַ���(7:3->07:03) 
	 * */
	public static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/**
	 * �������˺���ͷ��Ļ���
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
	 * �Ƚ��趨��ʱ������ڵ�ʱ��
	 * 
	 * @return 	false	ʱ���ѹ�
	 * 			true	ʱ��û��
	 */
	public static boolean compareTime(Calendar calendar) {
		if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}
}