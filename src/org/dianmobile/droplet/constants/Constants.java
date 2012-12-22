package org.dianmobile.droplet.constants;

import android.provider.BaseColumns;

/**
 * ��������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public interface Constants extends BaseColumns{
	
	/*������֤���*/
	/**������֤��URL*/
	public static final String RENREN_AUTH_URL = "http://" +
			"graph.renren.com/oauth/authorize?" +
			"client_id=a27cd23f525c4d88b9f083df79be4cb9" +
			"&response_type=token" +
			"&display=touch" +
			"&redirect_uri=http://graph.renren.com/oauth/login_success.html" +
			"&scope=";
	/**������֤��Ȩ�ޣ�����״̬���ϴ�ͼƬ*/
	public static final String RENREN_PERMISSIONS = 
			"status_update photo_upload";
	/**������֤��Secret Key*/
	public static final String RENREN_SECRET_KEY = "72fcfde91e434e5388717ffc3c65792e";
	/**����api�ĵ�ַ*/
	public static final String RENREN_API_URL = 
			"http://api.renren.com/restserver.do";
	/**���˵�id��ֵ*/
	public static final String RENREN_ID = "id";
	/**���˵��û�����ֵ*/
	public static final String RENREN_NAME = "name";
	/**���˵�ͷ���������Ӽ�ֵ*/
	public static final String RENREN_HEAD_URL = "headurl";
	
	/**�����֤��Ϣ��SharedPreference������*/
	public static final String AUTH_PREF_NAME = "auth_info";
	/**��֤���ݿ��е��û�����*/
	public static final String AUTH_RENREN_USER_NAME = "renren_username";
	/**��֤���ݿ��е������*/
	public static final String AUTH_RENREN_USER_CODE = "renren_usercode";
	/**��֤���ݿ��е�����accessToken*/
	public static final String AUTH_RENREN_ACCESS_TOKEN = "renren_access_token";
	
	/**ϰ��ѡ������*/
	public static final int WHAT_HABIT_RECITE_WORDS = 0;
	/**ϰ��ѡ������*/
	public static final int WHAT_HABIT_GET_UP = 1;
	/**ϰ��ѡ����˯*/
	public static final int WHAT_HABIT_JOGGING = 2;
	/**ϰ��ѡ���Զ���*/
	public static final int WHAT_HABIT_CUSTOM = 3;
	
	/**�ͷ�ѡ�����ҳԶ���*/
	public static final int WHAT_PUNISHMENT_EAT = 0;
	/**�ͷ�ѡ���Զ���*/
	public static final int WHAT_PUNISHMENT_CUSTOM = 1;
	
	/**����ˢ�½���*/
	public static final int REQUEST_CODE_REFRESH_VIEW = 0;
	/**��ת��@���˺��ѽ����������*/
	public static final int REQUSET_CODE_AT_RENREN_FRIENDS = 1;
	
	/**�Ի���ѡ������ѡ��*/
	public static final int CHOOSE_PICTURE_FROM_ALBUM = 0;
	/**�Ի���ѡ������ѡ��*/
	public static final int CHOOSE_PICTURE_FROM_CAMERA = 1;
	
	/**����״̬����*/
	public static final int ALARM_STATE_OFF = 0;
	/**����״̬����*/
	public static final int ALARM_STATE_ON = 1;
	
	/**�����͵���Ϣ��ˢ��EditText*/
	public static final int MSG_REFRESH_EDITTEXT = 0;
	
	/**bundle������*/
	public static final String BUNDLE_AT_INFO = "bundleAtInfo";
	
	/**����Intent�Ķ���*/
	public static final String ALARM_INTENT_ACTION = "org.dianmobile.droplet.receivers";
	
	/**���˼ල��ť������ַ�������*/
	public static final int MAX_BTN_TEXT_LENGTH = 20;
	
}