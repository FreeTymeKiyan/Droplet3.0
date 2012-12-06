package org.dianmobile.droplet.constants;

import android.provider.BaseColumns;

/**
 * 公共常量
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public interface Constants extends BaseColumns{
	
	/**存放认证信息的SharedPreference的名字*/
	public static final String AUTH_PREF_NAME = "auth_info";
	/**认证数据库中的用户名键*/
	public static final String AUTH_RENREN_USER_NAME = "username";
	/**认证数据库中的密码键*/
	public static final String AUTH_RENREN_USER_CODE = "usercode";
	
	/**习惯选择：早起*/
	public static final int WHAT_HABIT_GET_UP = 0;
	/**习惯选择：慢跑*/
	public static final int WHAT_HABIT_JOGGING = 1;
	/**习惯选择：早睡*/
	public static final int WHAT_HABIT_SLEEP = 2;
	/**习惯选择：自定义*/
	public static final int WHAT_HABIT_CUSTOM = 3;
	
	/**惩罚选择：请大家吃东西*/
	public static final int WHAT_PUNISHMENT_EAT = 0;
	/**惩罚选择：自定义*/
	public static final int WHAT_PUNISHMENT_CUSTOM = 1;
	
	/**请求：刷新界面*/
	public static final int REQUEST_CODE_REFRESH_VIEW = 0;
}