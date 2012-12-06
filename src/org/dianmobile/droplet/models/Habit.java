package org.dianmobile.droplet.models;

import static org.dianmobile.droplet.db.HabitDb.*;

import android.content.ContentValues;

/**
 * �����е���Ϣ����Habit���󷽱���д���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class Habit {
    /*��Ա����*/
	/**ȫ��Ψһ��ʶID*/
	public String uuid;
	/**ϰ�ߵ�����*/
	public String name;
	/**ϰ�߶�Ӧ�ĳͷ���ʩ*/
	public String punish;
	/**ϰ��@����*/
	public String followers;
	/**ϰ�ߵļ�¼*/
	public String words;
	/**ϰ�ߵ�ͼƬ*/
	public String picPath;
	/**��¼��ʱ���*/
	public String timestamp;
	/**���ӵ�״̬*/
	public int alarmState = STATE_OFF;
	/**���ӵ�ʱ��*/
	public String alarmTime;
	
	/*��Ա�����Ƿ���ڵ�״̬*/
	/**ȫ��Ψһ��ʶID��״̬*/
	public boolean boolUuid = false;
	/**ϰ�ߵ����ݵ�״̬*/
	public boolean boolName = false;
	/**ϰ�߶�Ӧ�ĳͷ���ʩ��״̬*/
	public boolean boolPunish = false;
	/**ϰ��@���˵�״̬*/
	public boolean boolFollowers = false;
	/**ϰ�ߵļ�¼��״̬*/
	public boolean boolWords = false;
	/**ϰ�ߵ�ͼƬ��״̬*/
	public boolean boolPicPath = false;
	/**��¼��ʱ�����״̬*/
	public boolean boolTimestamp = false;
	/**���ӵ�״̬��״̬*/
	public boolean boolAlarmState = false;
	/**���ӵ�ʱ���״̬*/
	public boolean boolAlarmTime = false;
	
	public Habit(ContentValues cv) {
		if (cv.containsKey(UUID)) {
			uuid = cv.getAsString(UUID);
			boolUuid = true;
		}
		if (cv.containsKey(NAME)) {
			name = cv.getAsString(NAME);
			boolName = true;
		}
		if (cv.containsKey(PUNISH)) {
			punish = cv.getAsString(PUNISH);
			boolPunish = true;
		}
		if (cv.containsKey(FOLLOWERS)) {
			followers = cv.getAsString(FOLLOWERS);
			boolFollowers = true;
		}
		if (cv.containsKey(WORDS)) {
			words = cv.getAsString(WORDS);
			boolWords = true;
		}
		if (cv.containsKey(TIMESTAMP)) {
			timestamp = cv.getAsString(TIMESTAMP);
			boolTimestamp = true;
		}
		if (cv.containsKey(PIC_PATH)) {
			picPath = cv.getAsString(PIC_PATH);
			boolPicPath = true;
		}
		if (cv.containsKey(STATE)) {
			alarmState = cv.getAsInteger(STATE);
			boolAlarmState = true;
		}
		if (cv.containsKey(TIME)) {
			alarmTime = cv.getAsString(TIME);
			boolAlarmTime = true;
		}
	}
}