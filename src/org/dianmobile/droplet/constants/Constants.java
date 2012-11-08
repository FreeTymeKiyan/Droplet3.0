package org.dianmobile.droplet.constants;

import android.provider.BaseColumns;

/**
 * 公共常量
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public interface Constants extends BaseColumns{
	
	/**存放认证信息的SharedPreference的名字*/
	public static final String AUTH_PREF_NAME = "auth_info";
	/**认证数据库中的用户名键*/
	public static final String AUTH_RENREN_USER_NAME = "username";
	/**认证数据库中的密码键*/
	public static final String AUTH_RENREN_USER_CODE = "usercode";
}