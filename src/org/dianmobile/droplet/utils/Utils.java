package org.dianmobile.droplet.utils;

import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ���ù�����
 * �������硢��ʽת����
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class Utils {
	
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
}