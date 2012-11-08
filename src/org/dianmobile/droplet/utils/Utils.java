package org.dianmobile.droplet.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 常用工具类
 * 如检查网络、格式转换等
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class Utils {
	
	/**
     * 检查网络连接的方法
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
}