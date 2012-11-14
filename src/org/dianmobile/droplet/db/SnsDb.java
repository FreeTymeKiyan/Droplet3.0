package org.dianmobile.droplet.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SNS的信息数据库
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class SnsDb {
	
	/**snsSharedPref的名字*/
	public static final String SNS = "sns";
	/**存储人人用户名的键名*/
	public static final String RENREN_USER = "s_renrenUser";
	/**存储人人用户密码的键名*/
	public static final String RENREN_PASSWORD = "s_renrenPassword";
	
	/**
	 * 存入人人网认证数据
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
	 * 删除人人网认证数据
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