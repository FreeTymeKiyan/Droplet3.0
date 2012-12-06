package org.dianmobile.droplet.constants;

import android.provider.BaseColumns;

/**
 * ��������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public interface Constants extends BaseColumns{
	
	/**�����֤��Ϣ��SharedPreference������*/
	public static final String AUTH_PREF_NAME = "auth_info";
	/**��֤���ݿ��е��û�����*/
	public static final String AUTH_RENREN_USER_NAME = "username";
	/**��֤���ݿ��е������*/
	public static final String AUTH_RENREN_USER_CODE = "usercode";
	
	/**ϰ��ѡ������*/
	public static final int WHAT_HABIT_GET_UP = 0;
	/**ϰ��ѡ������*/
	public static final int WHAT_HABIT_JOGGING = 1;
	/**ϰ��ѡ����˯*/
	public static final int WHAT_HABIT_SLEEP = 2;
	/**ϰ��ѡ���Զ���*/
	public static final int WHAT_HABIT_CUSTOM = 3;
	
	/**�ͷ�ѡ�����ҳԶ���*/
	public static final int WHAT_PUNISHMENT_EAT = 0;
	/**�ͷ�ѡ���Զ���*/
	public static final int WHAT_PUNISHMENT_CUSTOM = 1;
	
	/**����ˢ�½���*/
	public static final int REQUEST_CODE_REFRESH_VIEW = 0;
}