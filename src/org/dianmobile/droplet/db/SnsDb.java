package org.dianmobile.droplet.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SNS����Ϣ���ݿ�
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class SnsDb {
	
	/**snsSharedPref������*/
	public static final String SNS = "sns";
	/**�洢�����û����ļ���*/
	public static final String RENREN_USER = "s_renrenUser";
	/**�洢�����û�����ļ���*/
	public static final String RENREN_PASSWORD = "s_renrenPassword";
	
	/**
	 * ������������֤����
	 * 
	 * @param userName
	 * @param password
	 */
	public static void save(String userName, String password, Context context) {
		SharedPreferences snsSharedPref = context.getSharedPreferences(SNS, 
				Context.MODE_PRIVATE);
		Editor edit = snsSharedPref.edit();
		edit.putString(RENREN_USER, userName);
		edit.putString(RENREN_PASSWORD, password);
		edit.commit();
	}
	
	/**
	 * ɾ����������֤����
	 */
	public static void delete(Context context) {
		SharedPreferences snsSharedPref = context.getSharedPreferences(SNS, 
				Context.MODE_PRIVATE);
		Editor edit = snsSharedPref.edit();
		edit.remove(RENREN_USER);
		edit.remove(RENREN_PASSWORD);
		edit.commit();
	}
}