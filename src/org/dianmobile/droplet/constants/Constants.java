package org.dianmobile.droplet.constants;

import android.provider.BaseColumns;

/**
 * 公共常量
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public interface Constants extends BaseColumns{
	
	/*人人认证相关*/
	/**人人认证的URL*/
	public static final String RENREN_AUTH_URL = "http://" +
			"graph.renren.com/oauth/authorize?" +
			"client_id=a27cd23f525c4d88b9f083df79be4cb9" +
			"&response_type=token" +
			"&display=touch" +
			"&redirect_uri=http://graph.renren.com/oauth/login_success.html" +
			"&scope=";
	/**人人认证的权限：更新状态、上传图片*/
	public static final String RENREN_PERMISSIONS = 
			"status_update photo_upload";
	/**人人认证的Secret Key*/
	public static final String RENREN_SECRET_KEY = "72fcfde91e434e5388717ffc3c65792e";
	/**人人api的地址*/
	public static final String RENREN_API_URL = 
			"http://api.renren.com/restserver.do";
	/**人人的id键值*/
	public static final String RENREN_ID = "id";
	/**人人的用户名键值*/
	public static final String RENREN_NAME = "name";
	/**人人的头像下载链接键值*/
	public static final String RENREN_HEAD_URL = "headurl";
	
	/**存放认证信息的SharedPreference的名字*/
	public static final String AUTH_PREF_NAME = "auth_info";
	/**认证数据库中的用户名键*/
	public static final String AUTH_RENREN_USER_NAME = "renren_username";
	/**认证数据库中的密码键*/
	public static final String AUTH_RENREN_USER_CODE = "renren_usercode";
	/**认证数据库中的人人accessToken*/
	public static final String AUTH_RENREN_ACCESS_TOKEN = "renren_access_token";
	
	/**习惯选择：早起*/
	public static final int WHAT_HABIT_RECITE_WORDS = 0;
	/**习惯选择：慢跑*/
	public static final int WHAT_HABIT_GET_UP = 1;
	/**习惯选择：早睡*/
	public static final int WHAT_HABIT_JOGGING = 2;
	/**习惯选择：自定义*/
	public static final int WHAT_HABIT_CUSTOM = 3;
	
	/**惩罚选择：请大家吃东西*/
	public static final int WHAT_PUNISHMENT_EAT = 0;
	/**惩罚选择：自定义*/
	public static final int WHAT_PUNISHMENT_CUSTOM = 1;
	
	/**请求：刷新界面*/
	public static final int REQUEST_CODE_REFRESH_VIEW = 0;
	/**跳转到@人人好友界面的请求码*/
	public static final int REQUSET_CODE_AT_RENREN_FRIENDS = 1;
	
	/**对话框选项：从相册选择*/
	public static final int CHOOSE_PICTURE_FROM_ALBUM = 0;
	/**对话框选项：从相机选择*/
	public static final int CHOOSE_PICTURE_FROM_CAMERA = 1;
	
	/**闹钟状态：关*/
	public static final int ALARM_STATE_OFF = 0;
	/**闹钟状态：开*/
	public static final int ALARM_STATE_ON = 1;
	
	/**程序发送的消息：刷新EditText*/
	public static final int MSG_REFRESH_EDITTEXT = 0;
	
	/**bundle的名字*/
	public static final String BUNDLE_AT_INFO = "bundleAtInfo";
	
	/**闹钟Intent的动作*/
	public static final String ALARM_INTENT_ACTION = "org.dianmobile.droplet.receivers";
	
	/**请人监督按钮的最大字符串长度*/
	public static final int MAX_BTN_TEXT_LENGTH = 20;
	
}